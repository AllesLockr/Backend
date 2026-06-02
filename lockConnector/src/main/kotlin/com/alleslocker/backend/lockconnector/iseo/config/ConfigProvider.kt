package com.alleslocker.backend.lockconnector.iseo.config

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
        val username: String,
        val password: String,
    )

    fun load(api: AvailableVendors): ApiCredentials {
        val apiData = vendorGateway.findByForApi(api) ?: throw IllegalStateException("$api API data not found")
        val auth = apiData.vendorAuthentication
        require(auth is VendorAuthentication.BaseAuth) { "$api API data must use base auth" }
        return ApiCredentials(
            baseUrl = apiData.baseUrl.toString(),
            username = auth.username.value,
            password = auth.password.value,
        )
    }
}
