package com.alleslocker.backend.web.person.schema.request

data class CreatePersonRequestSchema(
    val firstname: String,
    val lastname: String,
    val email: String,
)