package com.alleslocker.backend.application.person.dto.request

data class CreatePersonRequestDto(
    val requesterId: String,
    val firstname: String,
    val lastname: String,
    val email: String,
)
