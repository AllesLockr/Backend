package com.alleslocker.backend.application.user.mapper

import com.alleslocker.backend.application.user.dto.UserDto
import com.alleslocker.backend.application.user.dto.request.GetUsersPagedRequestDto
import com.alleslocker.backend.domain.user.User

fun User.toDto() =
    UserDto(
        id = id.value,
        role = role.toDto(),
        firstname = firstname.value,
        lastname = lastname.value,
        username = username.value,
        email = email.value,
        isActive = isActive,
    )
