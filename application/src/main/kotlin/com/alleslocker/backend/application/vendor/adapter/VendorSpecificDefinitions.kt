package com.alleslocker.backend.application.vendor.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificDefinition

interface VendorSpecificDefinitions : Adapter {
    fun get(availableVendors: AvailableVendors): VendorSpecificDefinition?
}