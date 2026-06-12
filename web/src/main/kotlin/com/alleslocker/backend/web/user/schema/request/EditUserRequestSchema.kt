package com.alleslocker.backend.web.user.schema.request

data class EditUserRequestSchema(
    val userId: String,
    val firstname: String? = null,
    val lastname: String? = null,
    val username: String? = null,
    val email: String? = null,
)
