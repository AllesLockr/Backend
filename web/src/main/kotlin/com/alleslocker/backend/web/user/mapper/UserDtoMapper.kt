package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.UserDto
import com.alleslocker.backend.web.user.schema.UserSchema

fun UserDto.toSchema() =
    UserSchema(
        id = id,
        role = role.toSchema(),
        firstname = firstname,
        lastname = lastname,
        username = username,
        email = email,
        mustChangePassword = mustChangePassword,
        isActive = isActive,
    )
