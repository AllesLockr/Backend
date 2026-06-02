package com.alleslocker.backend.application.vendor.dto.response

data class GetVendorDataResponseDto(
    val id: String,
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
    val vendorConnectionState: String,
    val lastChecked: Long,
)
