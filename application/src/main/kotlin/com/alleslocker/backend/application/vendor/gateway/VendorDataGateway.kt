package com.alleslocker.backend.application.vendor.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorData
import com.alleslocker.backend.domain.vendor.VendorId

interface VendorDataGateway : ReadWriteGateway<VendorData, VendorId> {
    fun findByForApi(forApi: AvailableVendors): VendorData?

    fun findAll(): List<VendorData>
}
