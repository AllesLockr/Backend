package com.alleslocker.backend.web.user.schema.response

data class ResetPasswordUserResponseSchema(
    val userId: String,
    val jwtToken: String,
)
