package com.alleslocker.backend.web.user.schema.request

data class ResetPasswordUserRequestSchema(
    val requestorId: String,
    val oldPassword: String,
    val newPassword: String,
)
