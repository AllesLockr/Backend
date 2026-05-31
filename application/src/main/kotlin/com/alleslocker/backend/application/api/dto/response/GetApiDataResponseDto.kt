package com.alleslocker.backend.application.api.dto.response

data class GetApiDataResponseDto(
    val id: String,
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?,
)
