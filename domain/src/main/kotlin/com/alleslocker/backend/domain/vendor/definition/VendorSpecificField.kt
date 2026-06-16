package com.alleslocker.backend.domain.vendor.definition

data class VendorSpecificField(
    val name: String,
    val type: VendorSpecificFieldType,
    val internal: Boolean,
    val description: String,
)
