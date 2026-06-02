package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendor.dto.response.GetImplementedVendorsResponseDto
import com.alleslocker.backend.domain.vendor.AvailableVendors

class GetImplementedVendorsUseCaseImpl : GetImplementedVendorsUseCase {
    override fun execute(
        request: Unit,
        presenter: OutputBoundary<GetImplementedVendorsResponseDto>,
    ) {
        val response = GetImplementedVendorsResponseDto(apis = AvailableVendors.entries.map { it.name })
        presenter.present(response)
    }
}
