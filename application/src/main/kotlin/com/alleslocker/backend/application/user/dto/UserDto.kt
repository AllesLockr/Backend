package com.alleslocker.backend.application.user.dto

import com.alleslocker.backend.domain.user.UserRole

data class UserDto(
    val id: String,
    val role: UserRoleDto,
    val firstname: String,
    val lastname: String,
    val username: String,
    val email: String,
    val isActive: Boolean,
)