package com.alleslocker.backend.web.user.schema.request

data class ResetPasswordUserRequestSchema(
    val oldPassword: String,
    val newPassword: String,
)
