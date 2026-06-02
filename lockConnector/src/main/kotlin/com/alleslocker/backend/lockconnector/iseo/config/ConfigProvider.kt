package com.alleslocker.backend.lockconnector.iseo.config

import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import org.springframework.stereotype.Component

@Component
class ConfigProvider(
    private val apiGateway: VendorDataGateway,
) {
    data class IseoCredentials(
        val baseUrl: String,
        val username: String,
        val password: String,
    )

    fun load(): IseoCredentials {
        val apiData =
            apiGateway.findByForApi(AvailableVendors.ISEO) ?: throw IllegalStateException("ISEO API data not found")
        val auth = apiData.vendorAuthentication
        require(auth is VendorAuthentication.BaseAuth) { "ISEO API data must use base auth" }
        return IseoCredentials(
            baseUrl = apiData.baseUrl.toString(),
            username = auth.username.value,
            password = auth.password.value,
        )
    }
}
