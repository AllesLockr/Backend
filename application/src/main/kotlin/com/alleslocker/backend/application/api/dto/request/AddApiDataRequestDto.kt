package com.alleslocker.backend.application.api.dto.request

data class AddApiDataRequestDto(
    val requesterId: String,
    val forApi: String,
    val baseUrl: String,
    val apiKey: String?,
    val apiUsername: String?,
    val apiPassword: String?
)
