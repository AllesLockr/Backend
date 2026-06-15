package com.alleslocker.backend.application.vendor.dto.request

import com.alleslocker.backend.application.common.dto.MetadataEntryDto

data class AddVendorDataRequestDto(
    val requesterId: String,
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
    val metadata: Set<MetadataEntryDto>?,
)
