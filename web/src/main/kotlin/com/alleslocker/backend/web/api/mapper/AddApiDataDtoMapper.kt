package com.alleslocker.backend.web.api.mapper

import com.alleslocker.backend.application.api.dto.request.AddApiDataRequestDto
import com.alleslocker.backend.web.api.schema.AddApiDataRequestSchema

fun AddApiDataRequestSchema.toDto(requesterId: String) =
    AddApiDataRequestDto(
        forApi = forApi,
        baseUrl = baseUrl,
        apiKey = apiKey,
        apiUsername = apiUsername,
        apiPassword = apiPassword,
        requesterId = requesterId,
    )
