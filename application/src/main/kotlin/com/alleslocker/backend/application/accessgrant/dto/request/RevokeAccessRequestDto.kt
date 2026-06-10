package com.alleslocker.backend.application.accessgrant.dto.request

data class RevokeAccessRequestDto(
    val requesterId: String,
    val grantId: String,
)
