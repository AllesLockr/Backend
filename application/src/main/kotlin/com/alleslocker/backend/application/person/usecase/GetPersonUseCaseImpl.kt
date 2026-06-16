package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.request.GetPersonRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.mapper.toDto
import com.alleslocker.backend.domain.person.PersonId

internal class GetPersonUseCaseImpl(
    private val personGateway: PersonGateway,
    private val logger: Logger,
) : GetPersonUseCase {
    override fun execute(
        request: GetPersonRequestDto,
        presenter: OutputBoundary<GetPersonResponseDto>,
    ) {
        val personId =
            try {
                PersonId(request.personId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid person id: ${e.message}"))
                return
            }

        val person =
            try {
                personGateway.findById(personId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load person"))
                logger.error("Failed to load person with id ${request.personId}", e)
                return
            }

        if (person == null) {
            presenter.presentFailure(ErrorResponse.NotFound("Person with id ${request.personId} not found"))
            return
        }

        presenter.present(
            GetPersonResponseDto(
                person.toDto(),
            ),
        )
    }
}
