package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.dto.request.UpdateVendorDataRequestDto
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
import com.alleslocker.backend.domain.vendor.VendorState
import java.net.URI
import java.net.URISyntaxException
import java.time.Instant

class UpdateVendorDataUseCaseImpl(
    private val vendorDataGateway: VendorDataGateway,
    private val logger: Logger,
    private val vendorConnectionAdapter: VendorConnectionAdapter
) :
    UpdateVendorDataUseCase {
    override fun execute(
        request: UpdateVendorDataRequestDto,
        presenter: OutputBoundary<SuccessResponse>
    ) {
        val forApi = try {
            AvailableVendors.valueOf(request.forApi)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("forApi value invalid"))
            logger.error("No vendor found for ${request.forApi}", e)
            return
        }

        val existing = vendorDataGateway.findByForApi(forApi)
        if (existing == null) {
            presenter.presentFailure(ErrorResponse.NotFound("No configured vendor-data found for ${request.forApi}"))
            return
        }

        val baseUrl = let {
            if (request.baseUrl == null) {
                existing.baseUrl
            } else {
                try {
                    URI(request.baseUrl)
                } catch (_: URISyntaxException) {
                    presenter.presentFailure(ErrorResponse.BadRequest("baseUrl is not a valid URI"))
                    return
                }
            }
        }

        val vendorAuthentication =
            let {
                if (!request.apiKey.isNullOrBlank() && !request.apiUsername.isNullOrBlank() && !request.apiPassword.isNullOrBlank()) {
                    return presenter.presentFailure(ErrorResponse.UnprocessableEntity("you can only update apiKey OR username and password, not both!"))
                }
                if (!request.apiKey.isNullOrBlank() && request.apiUsername.isNullOrBlank() && request.apiPassword.isNullOrBlank()) {
                    VendorAuthentication.ApiKey(request.apiKey)
                } else if (request.apiKey.isNullOrBlank() && !request.apiUsername.isNullOrBlank() && !request.apiPassword.isNullOrBlank()) {
                    VendorAuthentication.BaseAuth(ApiUsername(request.apiUsername), ApiPassword(request.apiPassword))
                } else {
                    existing.vendorAuthentication
                }
            }

        val updated =
            VendorData(
                id = existing.id,
                forVendor = forApi,
                baseUrl = baseUrl,
                vendorAuthentication = vendorAuthentication,
                vendorState = VendorState(VendorConnectionState.DISCONNECTED, Instant.now()),
            )

        val saved =
            try {
                vendorDataGateway.save(updated)
            } catch (e: Exception) {
                logger.error("Could not save vendor-data to db: ", e)
                return presenter.presentFailure(ErrorResponse.InternalServerError("Could not save vendor-data to db."))
            }

        try {
            val vendorState = vendorConnectionAdapter.check(forApi)
            vendorDataGateway.save(saved.copy(vendorState = vendorState))
        } catch (e: Exception) {
            logger.error("Could not update connection state: ", e)
            return presenter.presentFailure(ErrorResponse.InternalServerError("Could not update connection state"))
        }
        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Updated Vendor-Data for ${saved.forVendor}: ${updatedFields(request)}"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )
        presenter.present(SuccessResponse.Created("Successfully updated ${saved.forVendor}"))
    }

    private fun updatedFields(request: UpdateVendorDataRequestDto): String {
        return buildList {
            if (!request.apiKey.isNullOrBlank()) add("ApiKey")
            if (!request.apiUsername.isNullOrBlank()) add("ApiUsername")
            if (!request.apiPassword.isNullOrBlank()) add("ApiPassword")
            if (!request.baseUrl.isNullOrBlank()) add("BaseUrl")
        }.joinToString(", ")
    }
}