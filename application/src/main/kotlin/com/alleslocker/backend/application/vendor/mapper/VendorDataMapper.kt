package com.alleslocker.backend.application.vendor.mapper

import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import com.alleslocker.backend.domain.vendor.VendorData

fun VendorData.toDto(): GetVendorDataResponseDto {
    var apiKey: String? = null
    var apiUsername: String? = null
    var apiPassword: String? = null

    when (val auth = this.vendorAuthentication) {
        is VendorAuthentication.ApiKey -> {
            apiKey = auth.value
        }

        is VendorAuthentication.BaseAuth -> {
            apiUsername = auth.username.value
            apiPassword = auth.password.value
        }
    }

    return GetVendorDataResponseDto(
        id = this.id.value,
        forApi = this.forVendor.toString(),
        baseUrl = this.baseUrl.toString(),
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword,
        vendorConnectionState = vendorState.connectionState.toString(),
        lastChecked = vendorState.lastChecked.toEpochMilli(),
    )
}
