package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.request.DeletePersonRequestDto
import com.alleslocker.backend.application.person.dto.response.DeletePersonResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.person.PersonId

internal class DeletePersonUseCaseImpl(
    private val personGateway: PersonGateway
) : DeletePersonUseCase {

    override fun execute(
        request: DeletePersonRequestDto,
        presenter: OutputBoundary<DeletePersonResponseDto>
    ) {
        val id = try {
            PersonId(request.id)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid person ID: ${e.message}"))
            return
        }

        if (!personGateway.exists(id)) {
            presenter.presentFailure(ErrorResponse.NotFound("Person with ID $id not found"))
            return
        }

        try {
            personGateway.deleteById(id)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to delete person: ${e.message ?: "Unknown error"}"))
            return
        }
        presenter.present(
            DeletePersonResponseDto(
                id = id.value
            )
        )
    }


}