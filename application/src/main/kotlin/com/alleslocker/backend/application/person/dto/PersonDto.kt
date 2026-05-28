package com.alleslocker.backend.application.person.dto

data class PersonDto(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val externalIds: Map<String, String> = emptyMap(),
    //TODO: Add roles
)