package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.dto.request.AddVendorDataRequestDto
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
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
) : AddVendorDataUseCase {
    override fun execute(
        request: AddVendorDataRequestDto,
        presenter: OutputBoundary<SuccessResponse>,
    ) {
        val forApi =
            try {
                AvailableVendors.valueOf(request.forApi)
            } catch (_: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.UnprocessableEntity("${request.forApi} is not implemented (yet)!"))
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
                if (!request.apiKey.isNullOrBlank() && request.apiUsername.isNullOrBlank() && request.apiPassword.isNullOrBlank()) {
                    VendorAuthentication.ApiKey(request.apiKey)
                } else if (request.apiKey.isNullOrBlank() && !request.apiUsername.isNullOrBlank() && !request.apiPassword.isNullOrBlank()) {
                    VendorAuthentication.BaseAuth(ApiUsername(request.apiUsername), ApiPassword(request.apiPassword))
                } else {
                    return presenter.presentFailure(ErrorResponse.BadRequest("either apiKey or apiUsername and apiPassword must be set!"))
                }
            }

        val vendorData =
            VendorData(
                id = VendorId.generate(),
                forVendor = forApi,
                baseUrl = baseUrl,
                vendorAuthentication = vendorAuthentication,
                vendorState = VendorState(VendorConnectionState.DISCONNECTED, Instant.now()),
            )

        val saved =
            try {
                vendorDataGateway.save(vendorData)
            } catch (e: Exception) {
                return presenter.presentFailure(ErrorResponse.InternalServerError("Could not save api to db: ${e.message}"))
            }

        val vendorState = vendorConnectionAdapter.check(forApi, null)

        try {
            vendorDataGateway.save(saved.copy(vendorState = vendorState))
        } catch (e: Exception) {
            return presenter.presentFailure(ErrorResponse.InternalServerError("Could not update connection state: ${e.message}"))
        }
        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Added new API-Data for ${saved.forVendor} with url ${saved.baseUrl}"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )
        presenter.present(SuccessResponse.Created("Successfully added ${saved.baseUrl}"))
    }
}
