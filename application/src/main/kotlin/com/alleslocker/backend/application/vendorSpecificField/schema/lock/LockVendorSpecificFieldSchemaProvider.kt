package com.alleslocker.backend.application.vendorSpecificField.schema.lock

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

interface LockVendorSpecificFieldSchemaProvider {
    val forVendor: AvailableVendors

    fun fields(): List<VendorSpecificFieldSchema>
}
