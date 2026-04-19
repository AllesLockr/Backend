package com.alleslocker.backend.web.user.schema.response

data class RegisterUserResponseSchema(
    val userId: String,
    val jwtToken: String
)