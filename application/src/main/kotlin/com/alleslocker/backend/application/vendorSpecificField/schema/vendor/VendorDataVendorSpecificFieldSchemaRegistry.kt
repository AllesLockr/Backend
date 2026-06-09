package com.alleslocker.backend.application.vendorSpecificField.schema.vendor

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

class VendorDataVendorSpecificFieldSchemaRegistry(
    providers: List<VendorDataVendorSpecificFieldSchemaProvider>,
) {
    private val providersByVendor: Map<AvailableVendors, VendorDataVendorSpecificFieldSchemaProvider> =
        providers.associateBy { it.forVendor }

    fun fields(forVendor: AvailableVendors): List<VendorSpecificFieldSchema> =
        providersByVendor[forVendor]?.fields() ?: emptyList()
}
