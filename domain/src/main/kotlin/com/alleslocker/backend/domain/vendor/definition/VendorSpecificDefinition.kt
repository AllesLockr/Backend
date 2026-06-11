package com.alleslocker.backend.domain.vendor.definition

import com.alleslocker.backend.domain.vendor.AvailableVendors

data class VendorSpecificDefinition(
    val vendorName: AvailableVendors,
    val vendorSpecificFields: List<VendorSpecificField>
)