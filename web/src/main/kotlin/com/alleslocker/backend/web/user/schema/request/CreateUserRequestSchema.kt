package com.alleslocker.backend.web.user.schema.request

data class CreateUserRequestSchema(
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
)
