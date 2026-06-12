package com.alleslocker.backend.application.user.dto.request

data class RequestUserPasswordChangeRequestDto(
    val requestorId: String,
    val userId: String,
)
