package com.alleslocker.backend.application.vendorSpecificField.schema.vendor.provider

import com.alleslocker.backend.application.vendorSpecificField.schema.vendor.VendorDataVendorSpecificFieldSchemaProvider
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.VendorSpecificFieldType
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

class IseoVendorDataSchemaProvider : VendorDataVendorSpecificFieldSchemaProvider {
    override val forVendor: AvailableVendors = AvailableVendors.ISEO

    override fun fields(): List<VendorSpecificFieldSchema> =
        listOf(
            VendorSpecificFieldSchema(name = "installer-email", type = VendorSpecificFieldType.EMAIL),
        )
}
