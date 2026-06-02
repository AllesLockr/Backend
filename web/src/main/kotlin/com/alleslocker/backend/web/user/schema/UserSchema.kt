package com.alleslocker.backend.web.user.schema

import com.alleslocker.backend.application.user.dto.UserRoleDto

data class UserSchema(
    val id: String,
    val role: UserRoleSchema,
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val isActive: Boolean,
)
