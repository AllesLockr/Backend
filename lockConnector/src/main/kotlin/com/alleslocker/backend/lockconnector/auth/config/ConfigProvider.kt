package com.alleslocker.backend.lockconnector.auth.config

import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
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
    )

    fun load(api: AvailableVendors): ApiCredentials {
        val apiData = vendorGateway.findByForApi(api) ?: throw IllegalStateException("$api Vendor data not found")

        return ApiCredentials(
            baseUrl = apiData.baseUrl.toString(),
            authentication = apiData.vendorAuthentication,
        )
    }
}
