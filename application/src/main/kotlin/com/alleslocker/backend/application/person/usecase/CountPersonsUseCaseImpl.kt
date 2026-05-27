package com.alleslocker.backend.application.person.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.request.CountPersonsRequestDto
import com.alleslocker.backend.application.person.dto.response.CountPersonsResponseDto
import com.alleslocker.backend.application.person.gateway.PersonGateway

internal class CountPersonsUseCaseImpl(
    private val personGateway: PersonGateway
) : CountPersonsUseCase {
    override fun execute(
        request: CountPersonsRequestDto,
        presenter: OutputBoundary<CountPersonsResponseDto>
    ) {
        val count = try {
            personGateway.count()
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to count persons: ${e.message ?: "Unknown error"}"))
            return
        }

        presenter.present(CountPersonsResponseDto(count))
    }
}