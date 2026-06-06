package com.alleslocker.backend.application.user.dto.request

data class CreateUserRequestDto(
    val requestorId: String,
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
)
