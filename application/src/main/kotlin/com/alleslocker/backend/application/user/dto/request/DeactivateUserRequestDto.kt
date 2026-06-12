package com.alleslocker.backend.application.user.dto.request

data class DeactivateUserRequestDto(
    val requestorId: String,
    val userId: String,
)
