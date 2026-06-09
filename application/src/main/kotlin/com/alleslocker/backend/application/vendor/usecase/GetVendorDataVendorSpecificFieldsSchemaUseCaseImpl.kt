package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.request.GetVendorSpecificFieldsSchemaRequestDto
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.response.GetVendorSpecificFieldsSchemaResponseDto
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.response.VendorSpecificFieldSchemaDto
import com.alleslocker.backend.application.vendorSpecificField.schema.vendor.VendorDataVendorSpecificFieldSchemaRegistry
import com.alleslocker.backend.domain.vendor.AvailableVendors

class GetVendorDataVendorSpecificFieldsSchemaUseCaseImpl(
    private val registry: VendorDataVendorSpecificFieldSchemaRegistry,
) : GetVendorDataVendorSpecificFieldsSchemaUseCase {
    override fun execute(
        request: GetVendorSpecificFieldsSchemaRequestDto,
        presenter: OutputBoundary<GetVendorSpecificFieldsSchemaResponseDto>,
    ) {
        val vendor =
            runCatching { AvailableVendors.valueOf(request.forVendor) }
                .getOrElse {
                    presenter.presentFailure(
                        ErrorResponse.UnprocessableEntity("Unknown vendor: ${request.forVendor}"),
                    )
                    return
                }

        val fields =
            registry.fields(vendor).map { VendorSpecificFieldSchemaDto(name = it.name, type = it.type.name) }

        presenter.present(GetVendorSpecificFieldsSchemaResponseDto(forVendor = vendor.name, fields = fields))
    }
}
