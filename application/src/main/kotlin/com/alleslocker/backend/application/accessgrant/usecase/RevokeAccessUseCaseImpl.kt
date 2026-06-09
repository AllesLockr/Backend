package com.alleslocker.backend.application.accessgrant.usecase

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.accessgrant.dto.request.RevokeAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.RevokeAccessResponseDto
import com.alleslocker.backend.application.accessgrant.gateway.AccessGrantGateway
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.domain.accessgrant.AccessGrantId
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserId
import java.time.Instant

internal class RevokeAccessUseCaseImpl(
    private val accessGrantGateway: AccessGrantGateway,
    private val accessGrantAdapter: AccessGrantAdapter,
    private val logger: Logger,
) : RevokeAccessUseCase {
    override fun execute(
        request: RevokeAccessRequestDto,
        presenter: OutputBoundary<RevokeAccessResponseDto>,
    ) {
        val grantId =
            try {
                AccessGrantId(request.grantId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid grantId: ${e.message}"))
                return
            }

        val grant =
            accessGrantGateway.findById(grantId)
                ?: run {
                    presenter.presentFailure(ErrorResponse.NotFound("Access grant ${grantId.value} not found"))
                    return
                }

        val identity =
            grant.apiIdentity
                ?: run {
                    presenter.presentFailure(
                        ErrorResponse.UnprocessableEntity("Access grant ${grantId.value} is not present on any vendor"),
                    )
                    return
                }
        val vendor = identity.api

        try {
            accessGrantAdapter.revoke(RevokeAccessAdapterRequest(vendor, identity.externalId.value))
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(
                ErrorResponse.UnprocessableEntity(e.message ?: "Access revoke not supported on $vendor"),
            )
            return
        } catch (e: Exception) {
            logger.error("Failed to revoke access grant ${grantId.value} on $vendor", e)
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to revoke access on $vendor"))
            return
        }

        try {
            accessGrantGateway.deleteById(grantId)
        } catch (e: Exception) {
            logger.error(
                "Vendor revoke for grant ${grantId.value} on $vendor succeeded but DB deletion failed; " +
                    "grant is stale and safe to retry",
                e,
            )
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to delete access grant"))
            return
        }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message =
                    AuditLogMessage(
                        "Revoked access: person ${grant.personId.value} -> lock ${grant.lockId.value} on $vendor (grant ${grantId.value})",
                    ),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )

        presenter.present(
            RevokeAccessResponseDto(
                grantId = grantId.value,
                vendor = vendor.name,
            ),
        )
    }
}
