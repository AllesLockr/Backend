package com.alleslocker.backend.application.user.dto.request

data class ResetPasswordUserRequestDto(
    val requestorId: String,
    val oldPassword: String,
    val newPassword: String,
)
