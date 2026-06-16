package com.alleslocker.backend.lockconnector.auth.config

import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import org.springframework.stereotype.Component

@Component
class ConfigProvider(
    private val vendorGateway: VendorDataGateway,
) {
    data class ApiCredentials(
        val baseUrl: String,
        val authentication: VendorAuthentication,
        val metadata: Set<MetadataEntry> = emptySet(),
    )

    fun configuredVendors(): Set<AvailableVendors> = vendorGateway.findAll().map { it.forVendor }.toSet()

    fun load(api: AvailableVendors): ApiCredentials {
        val apiData = vendorGateway.findByForApi(api) ?: throw IllegalStateException("$api Vendor data not found")

        return ApiCredentials(
            baseUrl = apiData.baseUrl.toString(),
            authentication = apiData.vendorAuthentication,
            metadata = apiData.metadata,
        )
    }
}
