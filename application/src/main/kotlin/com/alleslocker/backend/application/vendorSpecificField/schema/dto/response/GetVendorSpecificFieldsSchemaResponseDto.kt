package com.alleslocker.backend.application.vendorSpecificField.schema.dto.response

data class GetVendorSpecificFieldsSchemaResponseDto(
    val forVendor: String,
    val fields: List<VendorSpecificFieldSchemaDto>,
)
