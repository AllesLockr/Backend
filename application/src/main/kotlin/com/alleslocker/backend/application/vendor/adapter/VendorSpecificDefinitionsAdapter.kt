package com.alleslocker.backend.application.vendor.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.common.dto.MetadataEntryDto
import com.alleslocker.backend.domain.shared.MetadataValidationResult
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificDefinition

interface VendorSpecificDefinitionsAdapter : Adapter {
    fun get(availableVendors: AvailableVendors): VendorSpecificDefinition?

    fun validateMetadataRequest(
        forVendor: AvailableVendors,
        metadata: Set<MetadataEntryDto>?,
    ): MetadataValidationResult
}
