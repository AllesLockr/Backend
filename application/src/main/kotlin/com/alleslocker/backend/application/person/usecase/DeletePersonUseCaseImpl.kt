package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.DeletePersonRequestDto
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.DeletePersonResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.user.UserId
import java.time.Instant

internal class DeletePersonUseCaseImpl(
    private val personGateway: PersonGateway,
    private val personAdapter: PersonAdapter,
    private val logger: Logger,
) : DeletePersonUseCase {
    override fun execute(
        request: DeletePersonRequestDto,
        presenter: OutputBoundary<DeletePersonResponseDto>,
    ) {
        val id =
            try {
                PersonId(request.id)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid person ID: ${e.message}"))
                return
            }

        val person = personGateway.findById(id)
        if (person == null) {
            presenter.presentFailure(ErrorResponse.NotFound("Person with ID ${id.value} not found"))
            return
        }

        if (person.apiIdentities.isNotEmpty()) {
            val externalIds = person.apiIdentities.associate { it.api to it.externalId.value }
            try {
                personAdapter.deletePerson(DeletePersonAdapterRequest(externalIds = externalIds))
            } catch (e: Exception) {
                presenter.presentFailure(
                    ErrorResponse.InternalServerError(
                        "Failed to delete person on external API: ${e.message ?: "Unknown error"}",
                    ),
                )
                return
            }
        }

        try {
            personGateway.deleteById(id)
        } catch (e: Exception) {
            presenter.presentFailure(
                ErrorResponse.InternalServerError("Failed to delete person: ${e.message ?: "Unknown error"}"),
            )
            return
        }
        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Deleted person ${id.value}"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )
        presenter.present(
            DeletePersonResponseDto(
                id = id.value,
            ),
        )
    }
}
