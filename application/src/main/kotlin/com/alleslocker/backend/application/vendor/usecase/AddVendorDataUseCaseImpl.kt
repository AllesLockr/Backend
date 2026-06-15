package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitionsAdapter
import com.alleslocker.backend.application.vendor.dto.request.AddVendorDataRequestDto
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.shared.MetadataValidationResult
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.vendor.ApiPassword
import com.alleslocker.backend.domain.vendor.ApiUsername
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorData
import com.alleslocker.backend.domain.vendor.VendorId
import com.alleslocker.backend.domain.vendor.VendorState
import java.net.URI
import java.net.URISyntaxException
import java.time.Instant

class AddVendorDataUseCaseImpl(
    private val vendorDataGateway: VendorDataGateway,
    private val vendorConnectionAdapter: VendorConnectionAdapter,
    private val logger: Logger,
    private val vendorSpecificDefinitionsAdapter: VendorSpecificDefinitionsAdapter,
) : AddVendorDataUseCase {
    override fun execute(
        request: AddVendorDataRequestDto,
        presenter: OutputBoundary<SuccessResponse>,
    ) {
        val forApi =
            try {
                AvailableVendors.valueOf(request.forApi)
            } catch (_: IllegalArgumentException) {
                presenter.presentFailure(
                    ErrorResponse.UnprocessableEntity("${request.forApi} is not implemented (yet)!"),
                )
                return
            }

        val baseUrl =
            try {
                URI(request.baseUrl)
            } catch (_: URISyntaxException) {
                presenter.presentFailure(ErrorResponse.BadRequest("baseUrl is not a valid URI"))
                return
            } catch (_: NullPointerException) {
                presenter.presentFailure(ErrorResponse.BadRequest("baseUrl must not be null"))
                return
            }

        val vendorAuthentication =
            let {
                if (!request.apiKey.isNullOrBlank() && request.apiUsername.isNullOrBlank() &&
                    request.apiPassword.isNullOrBlank()
                ) {
                    VendorAuthentication.ApiKey(request.apiKey)
                } else if (request.apiKey.isNullOrBlank() && !request.apiUsername.isNullOrBlank() &&
                    !request.apiPassword.isNullOrBlank()
                ) {
                    VendorAuthentication.BaseAuth(ApiUsername(request.apiUsername), ApiPassword(request.apiPassword))
                } else {
                    return presenter.presentFailure(
                        ErrorResponse.BadRequest("either apiKey or apiUsername and apiPassword must be set!"),
                    )
                }
            }

        val metadata =
            let {
                val validationResult =
                    vendorSpecificDefinitionsAdapter.validateMetadataRequest(forApi, request.metadata)

                when (validationResult) {
                    is MetadataValidationResult.Error -> {
                        return presenter.presentFailure(
                            ErrorResponse.BadRequest(
                                validationResult.message,
                            ),
                        )
                    }

                    is MetadataValidationResult.Success -> {
                        request.metadata
                            ?.map { MetadataEntry(it.key, it.value) }
                            ?.toSet()
                            ?: emptySet()
                    }
                }
            }

        val vendorData =
            VendorData(
                id = VendorId.generate(),
                forVendor = forApi,
                baseUrl = baseUrl,
                vendorAuthentication = vendorAuthentication,
                vendorState = VendorState(VendorConnectionState.DISCONNECTED, Instant.now()),
                metadata = metadata,
            )

        val saved =
            try {
                vendorDataGateway.save(vendorData)
            } catch (e: Exception) {
                logger.error("Could not save vendor data to db: ${e.message}", e)
                return presenter.presentFailure(
                    ErrorResponse.InternalServerError("Could not save vendor data."),
                )
            }

        val checked =
            try {
                val vendorState = vendorConnectionAdapter.check(forApi)
                vendorDataGateway.save(vendorData.copy(vendorState = vendorState))
            } catch (e: Exception) {
                logger.error("Could not update connection state: ${e.message}", e)
                return presenter.presentFailure(
                    ErrorResponse.InternalServerError("Could not update connection state."),
                )
            }
        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Added new Vendor-Data for ${saved.forVendor} with url ${saved.baseUrl}"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )

        val updatedMetadata =
            try {
                vendorConnectionAdapter.handleMetadata(saved.forVendor, saved.metadata)
            } catch (e: Exception) {
                logger.error("Could not handle metadata: ${e.message}", e)
                return presenter.presentFailure(
                    ErrorResponse.InternalServerError("Vendor data saved but metadata handling failed."),
                )
            }

        try {
            vendorDataGateway.save(checked.copy(metadata = updatedMetadata))
        } catch (e: Exception) {
            logger.error("Could not update metadata: ${e.message}", e)
            return presenter.presentFailure(
                ErrorResponse.InternalServerError("Could not update metadata."),
            )
        }

        presenter.present(SuccessResponse.Created("Successfully added ${saved.baseUrl}"))
    }
}
