package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.application.vendor.mapper.toDto

class GetAllVendorDataUseCaseImpl(
    private val apiGateway: VendorDataGateway,
    private val logger: Logger
) : GetAllVendorDataUseCase {
    override fun execute(
        request: Unit,
        presenter: OutputBoundary<List<GetVendorDataResponseDto>>,
    ) {
        try {
            val apiDataList = apiGateway.findAll()

            val response = apiDataList.map { it.toDto() }

            presenter.present(response)
        } catch (e: Exception) {
            logger.error(e.message ?: "Unknown", e)
            presenter.presentFailure(ErrorResponse.InternalServerError(e.message ?: "Internal Server Error"))
        }
    }
}
