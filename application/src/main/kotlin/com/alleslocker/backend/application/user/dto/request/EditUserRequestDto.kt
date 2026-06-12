package com.alleslocker.backend.application.user.dto.request

data class EditUserRequestDto(
    val requestorId: String,
    val userId: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val username: String? = null,
    val email: String? = null,
)
