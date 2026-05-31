package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.CreatePersonRequestDto
import com.alleslocker.backend.application.person.dto.response.CreatePersonResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.api.ExternalId
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.person.*
import com.alleslocker.backend.domain.user.UserId
import java.time.Instant

internal class CreatePersonUseCaseImpl(
    private val personGateway: PersonGateway,
    private val personAdapter: PersonAdapter,
    private val logger: Logger,
) : CreatePersonUseCase {

    override fun execute(
        request: CreatePersonRequestDto,
        presenter: OutputBoundary<CreatePersonResponseDto>
    ) {
        val email = try {
            PersonEmail(request.email)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid email: ${e.message}"))
            return
        }

        val firstname = try {
            PersonFirstname(request.firstname)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid firstname: ${e.message}"))
            return
        }

        val lastname = try {
            PersonLastname(request.lastname)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid lastname: ${e.message}"))
            return
        }

        if (personGateway.existsByEmail(request.email)) {
            presenter.presentFailure(ErrorResponse.AlreadyExists("Person with email ${request.email} already exists"))
            return
        }

        val person = Person(
            id = PersonId.generate(),
            firstname = firstname,
            lastname = lastname,
            email = email,
            roles = emptySet()
        )

        val saved = try {
            personGateway.save(person)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save person: ${e.message ?: "Unknown error"}"))
            return
        }
        val adapterResponse = try {
            personAdapter.addPerson(
                AddPersonAdapterRequest(
                    firstname = firstname.value,
                    lastname = lastname.value,
                    email = email.value,
                    id = saved.id.value
                )
            )
        } catch (e: Exception) {
            personGateway.deleteById(saved.id)
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to send to API: ${e.message ?: "Unknown error"}"))
            return
        }
        val apiIdentities = try {
            adapterResponse.externalIds
                .map { (api, id) -> ExternalApiIdentity(api, ExternalId(id)) }
                .toSet()
        } catch (e: IllegalArgumentException) {
            personGateway.deleteById(saved.id)
            presenter.presentFailure(ErrorResponse.InternalServerError("External API returned invalid person IDs"))
            return
        }
        if (apiIdentities.isEmpty()) {
            personGateway.deleteById(saved.id)
            presenter.presentFailure(ErrorResponse.InternalServerError("External API did not return any person IDs"))
            return
        }

        try {
            personGateway.save(saved.copy(apiIdentities = apiIdentities))
        } catch (e: Exception) {
            personGateway.deleteById(saved.id)
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save external IDs"))
            return
        }


        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Created person ${saved.id.value} (${firstname.value} ${lastname.value})"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            )
        )
        presenter.present(
            CreatePersonResponseDto(
                id = saved.id.value
            )
        )
    }
}