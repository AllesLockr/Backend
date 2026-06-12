package com.alleslocker.backend.application.user.mapper

import com.alleslocker.backend.application.user.dto.UserRoleDto
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserRole

fun UserRole.toDto() =
    when (this) {
        UserRole.ADMIN -> UserRoleDto.ADMIN
        UserRole.USER -> UserRoleDto.USER
    }

fun UserRoleDto.toDomain() =
    when (this) {
        UserRoleDto.USER -> UserRole.USER
        UserRoleDto.ADMIN -> UserRole.ADMIN
    }
