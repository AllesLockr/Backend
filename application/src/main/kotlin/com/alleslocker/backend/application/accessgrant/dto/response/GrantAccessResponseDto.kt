package com.alleslocker.backend.application.accessgrant.dto.response

data class GrantAccessResponseDto(
    val grantId: String,
    val vendor: String,
    val vendorExternalId: String,
)
