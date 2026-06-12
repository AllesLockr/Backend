package com.alleslocker.backend.application.user.dto.request

data class ActivateUserRequestDto(
    val requestorId: String,
    val userId: String,
)
