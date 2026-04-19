package com.alleslocker.backend.web.user.schema.request

data class RegisterUserRequestSchema(
    val username: String,
    val password: String
)