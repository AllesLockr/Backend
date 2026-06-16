package com.alleslocker.backend.application.vendor.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorState

interface VendorConnectionAdapter : Adapter {
    fun check(vendor: AvailableVendors): VendorState

    fun handleMetadata(
        vendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    ): Set<MetadataEntry>

    fun handleMetadataOnDelete(
        forVendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    )
}
