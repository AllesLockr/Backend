package com.alleslocker.backend.application.vendorSpecificField.schema.vendor

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

interface VendorDataVendorSpecificFieldSchemaProvider {
    val forVendor: AvailableVendors

    fun fields(): List<VendorSpecificFieldSchema>
}
