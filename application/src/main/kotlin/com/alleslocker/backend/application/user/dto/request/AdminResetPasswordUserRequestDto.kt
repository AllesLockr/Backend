package com.alleslocker.backend.application.user.dto.request

data class AdminResetPasswordUserRequestDto(
    val requestorId: String,
    val userId: String,
)
