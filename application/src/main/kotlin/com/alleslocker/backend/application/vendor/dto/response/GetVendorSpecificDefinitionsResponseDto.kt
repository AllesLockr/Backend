package com.alleslocker.backend.application.vendor.dto.response

data class GetVendorSpecificDefinitionsResponseDto(
    val vendorName: String,
    val vendorSpecificFields: List<VendorSpecificFieldDto>,
)
