package com.alleslocker.backend.application.person.dto.request

data class DeletePersonRequestDto(
    val requesterId: String,
    val id: String
)