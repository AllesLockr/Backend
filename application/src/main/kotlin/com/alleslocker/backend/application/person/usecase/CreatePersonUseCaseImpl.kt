package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.request.CreatePersonRequestDto
import com.alleslocker.backend.application.person.dto.response.CreatePersonResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.person.*

internal class CreatePersonUseCaseImpl(
    private val personGateway: PersonGateway
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

        presenter.present(
            CreatePersonResponseDto(
                id = saved.id.value
            )
        )
    }
}