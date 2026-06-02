package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.application.vendor.mapper.toDto
import com.alleslocker.backend.domain.vendor.VendorId

class GetVendorDataUseCaseImpl(
    private val vendorDataGateway: VendorDataGateway,
) : GetVendorDataUseCase {
    override fun execute(
        request: IdRequest,
        presenter: OutputBoundary<GetVendorDataResponseDto>,
    ) {
        if (request.id.isBlank()) {
            presenter.presentFailure(ErrorResponse.BadRequest("ID cannot be blank"))
            return
        }

        val apiData = vendorDataGateway.findById(VendorId(request.id))

        if (apiData == null) {
            presenter.presentFailure(ErrorResponse.NotFound("ID not found"))
            return
        }

        val response = apiData.toDto()

        return presenter.present(response)
    }
}
