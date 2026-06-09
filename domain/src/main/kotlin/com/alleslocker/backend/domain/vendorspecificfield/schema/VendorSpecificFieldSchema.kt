package com.alleslocker.backend.domain.vendorspecificfield.schema

import com.alleslocker.backend.domain.vendorspecificfield.VendorSpecificFieldType

data class VendorSpecificFieldSchema(
    val name: String,
    val type: VendorSpecificFieldType,
)
