package com.alleslocker.backend.application.vendorSpecificField.schema.lock.provider

import com.alleslocker.backend.application.vendorSpecificField.schema.lock.LockVendorSpecificFieldSchemaProvider
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.VendorSpecificFieldType
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

class IseoLockSchemaProvider : LockVendorSpecificFieldSchemaProvider {
    override val forVendor: AvailableVendors = AvailableVendors.ISEO

    override fun fields(): List<VendorSpecificFieldSchema> =
        listOf(
            VendorSpecificFieldSchema(name = "TagID", type = VendorSpecificFieldType.TEXT),
        )
}
