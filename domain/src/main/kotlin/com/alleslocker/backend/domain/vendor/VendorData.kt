package com.alleslocker.backend.domain.vendor

import com.alleslocker.backend.domain.shared.MetadataEntry
import java.net.URI

data class VendorData(
    val id: VendorId,
    val forVendor: AvailableVendors,
    val baseUrl: URI,
    val vendorAuthentication: VendorAuthentication,
    val vendorState: VendorState,
    val metadata: Set<MetadataEntry> = emptySet(),
) {
    init {
        require(baseUrl.toString().isNotEmpty()) { "baseUrl must not be empty" }
    }
}
