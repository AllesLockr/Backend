package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.response.GetImplementedApisResponseDto
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.domain.api.AvailableApis

class GetImplementedApisUseCaseImpl : GetImplementedApisUseCase {
    override fun execute(
        request: Unit,
        presenter: OutputBoundary<GetImplementedApisResponseDto>
    ) {
        val response = GetImplementedApisResponseDto(apis = AvailableApis.entries.map { it.name })
        presenter.present(response)
    }
}