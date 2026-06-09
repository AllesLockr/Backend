package com.alleslocker.backend.application.vendorSpecificField.schema.lock

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendorspecificfield.schema.VendorSpecificFieldSchema

class LockVendorSpecificFieldSchemaRegistry(
    providers: List<LockVendorSpecificFieldSchemaProvider>,
) {
    private val providersByVendor: Map<AvailableVendors, LockVendorSpecificFieldSchemaProvider> =
        providers.associateBy { it.forVendor }

    fun fields(forVendor: AvailableVendors): List<VendorSpecificFieldSchema> =
        providersByVendor[forVendor]?.fields() ?: emptyList()
}
