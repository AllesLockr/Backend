package com.alleslocker.backend.application.vendor.dto.response

import com.alleslocker.backend.application.common.dto.MetadataEntryDto

data class GetVendorDataResponseDto(
    val id: String,
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
    val vendorConnectionState: String,
    val lastChecked: Long,
    val metadata: Set<MetadataEntryDto>?,
)
