package com.alleslocker.backend.web.vendor.mapper

import com.alleslocker.backend.application.vendor.dto.request.AddVendorDataRequestDto
import com.alleslocker.backend.web.vendor.schema.AddApiDataRequestSchema

fun AddApiDataRequestSchema.toDto(requesterId: String) =
    AddVendorDataRequestDto(
        forApi = forApi,
        baseUrl = baseUrl,
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword,
        requesterId = requesterId,
    )
