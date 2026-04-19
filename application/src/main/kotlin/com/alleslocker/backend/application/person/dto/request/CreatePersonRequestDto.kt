package com.alleslocker.backend.application.person.dto.request

data class CreatePersonRequestDto(
    val firstname: String,
    val lastname: String,
    val email: String,
)