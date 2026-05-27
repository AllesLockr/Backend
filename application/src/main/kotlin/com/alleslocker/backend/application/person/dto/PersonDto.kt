package com.alleslocker.backend.application.person.dto

data class PersonDto(
    val id: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    //TODO: Add roles
)