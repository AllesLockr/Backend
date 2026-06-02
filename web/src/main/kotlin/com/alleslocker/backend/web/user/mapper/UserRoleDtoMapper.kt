package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.UserRoleDto
import com.alleslocker.backend.web.user.schema.UserRoleSchema

fun UserRoleDto.toSchema() =
    when (this) {
        UserRoleDto.ADMIN -> UserRoleSchema.ADMIN
        UserRoleDto.USER -> UserRoleSchema.USER
    }

fun UserRoleSchema.toDto() =
    when (this) {
        UserRoleSchema.USER -> UserRoleDto.USER
        UserRoleSchema.ADMIN -> UserRoleDto.ADMIN
    }
