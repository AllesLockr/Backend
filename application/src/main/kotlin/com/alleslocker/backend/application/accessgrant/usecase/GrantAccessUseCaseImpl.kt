package com.alleslocker.backend.application.accessgrant.usecase

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.accessgrant.dto.request.GrantAccessRequestDto
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessResponseDto
import com.alleslocker.backend.application.accessgrant.gateway.AccessGrantGateway
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.accessgrant.AccessGrant
import com.alleslocker.backend.domain.accessgrant.AccessGrantId
import com.alleslocker.backend.domain.accessgrant.AccessSchedule
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
import java.time.Instant

internal class GrantAccessUseCaseImpl(
    private val accessGrantGateway: AccessGrantGateway,
    private val personGateway: PersonGateway,
    private val lockGateway: LockGateway,
    private val accessGrantAdapter: AccessGrantAdapter,
    private val logger: Logger,
) : GrantAccessUseCase {
    override fun execute(
        request: GrantAccessRequestDto,
        presenter: OutputBoundary<GrantAccessResponseDto>,
    ) {
        val personId =
            try {
                PersonId(request.personId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid personId: ${e.message}"))
                return
            }

        val lockId =
            try {
                LockId(request.lockId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid lockId: ${e.message}"))
                return
            }

        val schedule =
            try {
                AccessSchedule(start = request.start, end = request.end)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid schedule: ${e.message}"))
                return
            }

        val lock =
            lockGateway.findById(lockId)
                ?: run {
                    presenter.presentFailure(ErrorResponse.NotFound("Lock ${lockId.value} not found"))
                    return
                }

        val person =
            personGateway.findById(personId)
                ?: run {
                    presenter.presentFailure(ErrorResponse.NotFound("Person ${personId.value} not found"))
                    return
                }

        val lockIdentity =
            lock.apiIdentity
                ?: run {
                    presenter.presentFailure(ErrorResponse.BadRequest("Lock ${lockId.value} is not present on any vendor"))
                    return
                }
        val vendor = lockIdentity.api

        val personIdentity =
            person.apiIdentities.firstOrNull { it.api == vendor }
                ?: run {
                    presenter.presentFailure(ErrorResponse.UnprocessableEntity("Person ${personId.value} not present on $vendor"))
                    return
                }

        val lockMetadata = lock.metadata.associate { it.key to it.value }

        val grantId = AccessGrantId.generate()

        val adapterResponse =
            try {
                accessGrantAdapter.grant(
                    GrantAccessAdapterRequest(
                        vendor = vendor,
                        grantId = grantId.value,
                        personExternalId = personIdentity.externalId.value,
                        lockExternalId = lockIdentity.externalId.value,
                        metadata = lockMetadata,
                        start = schedule.start,
                        end = schedule.end,
                    ),
                )
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(
                    ErrorResponse.UnprocessableEntity(e.message ?: "Access grant not supported on $vendor"),
                )
                return
            } catch (e: Exception) {
                logger.error("Failed to push access grant to $vendor", e)
                presenter.presentFailure(
                    ErrorResponse.InternalServerError("Failed to grant access on $vendor"),
                )
                return
            }

        val grant =
            AccessGrant(
                id = grantId,
                personId = personId,
                lockId = lockId,
                schedule = schedule,
                apiIdentity = ExternalApiIdentity(vendor, ExternalId(adapterResponse.externalId)),
            )

        try {
            accessGrantGateway.save(grant)
        } catch (e: Exception) {
            logger.error("Failed to save access grant ${grantId.value}", e)
            runCatching {
                accessGrantAdapter.revoke(RevokeAccessAdapterRequest(vendor, adapterResponse.externalId))
            }.onFailure { logger.error("Failed to revoke grant ${grantId.value} on $vendor", it) }
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save access grant"))
            return
        }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message =
                    AuditLogMessage(
                        "Granted person ${personId.value} access to lock ${lockId.value} on $vendor (grant ${grantId.value})",
                    ),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )

        presenter.present(
            GrantAccessResponseDto(
                grantId = grantId.value,
                vendor = vendor.name,
                vendorExternalId = adapterResponse.externalId,
            ),
        )
    }
}
