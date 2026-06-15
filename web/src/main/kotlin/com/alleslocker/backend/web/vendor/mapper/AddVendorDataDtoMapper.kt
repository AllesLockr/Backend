package com.alleslocker.backend.web.vendor.mapper

import com.alleslocker.backend.application.common.dto.MetadataEntryDto
import com.alleslocker.backend.application.vendor.dto.request.AddVendorDataRequestDto
import com.alleslocker.backend.web.vendor.schema.AddVendorDataRequestSchema

fun AddVendorDataRequestSchema.toDto(requesterId: String) =
    AddVendorDataRequestDto(
        forApi = forApi,
        baseUrl = baseUrl,
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword,
        requesterId = requesterId,
        metadata = metadata?.map { MetadataEntryDto(it.key, it.value) }?.toSet(),
    )
