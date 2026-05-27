package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.request.GetPersonsPagedRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonsPagedResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.mapper.toDto
import com.alleslocker.backend.domain.person.Person

internal class GetPersonsPagedUseCaseImpl(
    private val personGateway: PersonGateway
) : GetPersonsPagedUseCase {
    override fun execute(
        request: GetPersonsPagedRequestDto,
        presenter: OutputBoundary<GetPersonsPagedResponseDto>
    ) {
        if (request.page < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Page must be higher than zero"))
            return
        }

        if (request.size < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Size must be higher than zero"))
            return
        }

        val page = try {
            personGateway.getAllPersonsPaged(
                filter = request.filter,
                page = request.page,
                size = request.size
            )
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load persons: ${e.message ?: "Unknown error"}"))
            return
        }

        presenter.present(
            GetPersonsPagedResponseDto(
                page.map { it.toDto() }
            )
        )
    }
}