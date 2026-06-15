package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitionsAdapter
import com.alleslocker.backend.application.vendor.dto.request.GetVendorSpecificDefinitionsRequestDto
import com.alleslocker.backend.application.vendor.dto.response.GetVendorSpecificDefinitionsResponseDto
import com.alleslocker.backend.application.vendor.dto.response.VendorSpecificFieldDto
import com.alleslocker.backend.domain.vendor.AvailableVendors

class GetVendorSpecificDefinitionsUseCaseImpl(
    private val vendorSpecificDefinitionsAdapter: VendorSpecificDefinitionsAdapter,
) : GetVendorSpecificDefinitionsUseCase {
    override fun execute(
        request: GetVendorSpecificDefinitionsRequestDto,
        presenter: OutputBoundary<GetVendorSpecificDefinitionsResponseDto>,
    ) {
        val forVendor =
            try {
                AvailableVendors.valueOf(request.forVendor)
            } catch (_: IllegalArgumentException) {
                return presenter.presentFailure(
                    ErrorResponse.UnprocessableEntity("${request.forVendor} is not an implemented vendor."),
                )
            }

        val definition =
            vendorSpecificDefinitionsAdapter.get(forVendor) ?: return presenter.presentFailure(
                ErrorResponse.NotFound("No vendor specific definition for ${request.forVendor} found!"),
            )

        presenter.present(
            GetVendorSpecificDefinitionsResponseDto(
                vendorName = definition.vendorName.name,
                vendorSpecificFields =
                    definition.vendorSpecificFields.filter { !it.internal }.map {
                        VendorSpecificFieldDto(
                            it.name,
                            it.type.name,
                        )
                    },
            ),
        )
    }
}
