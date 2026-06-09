package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.request.GetVendorSpecificFieldsSchemaRequestDto
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.response.GetVendorSpecificFieldsSchemaResponseDto
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.response.VendorSpecificFieldSchemaDto
import com.alleslocker.backend.application.vendorSpecificField.schema.lock.LockVendorSpecificFieldSchemaRegistry
import com.alleslocker.backend.domain.vendor.AvailableVendors

class GetLockVendorSpecificFieldsSchemaUseCaseImpl(
    private val registry: LockVendorSpecificFieldSchemaRegistry,
) : GetLockVendorSpecificFieldsSchemaUseCase {
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
