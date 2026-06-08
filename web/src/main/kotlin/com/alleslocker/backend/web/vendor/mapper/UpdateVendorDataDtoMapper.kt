package com.alleslocker.backend.web.vendor.mapper

import com.alleslocker.backend.application.vendor.dto.request.UpdateVendorDataRequestDto
import com.alleslocker.backend.web.vendor.schema.UpdateVendorDataRequestSchema

fun UpdateVendorDataRequestSchema.toDto(requesterId: String) =
    UpdateVendorDataRequestDto(
        requesterId = requesterId,
        forApi = forApi,
        baseUrl = baseUrl,
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword,
    )