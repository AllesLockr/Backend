package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema

fun LoginUserRequestSchema.toDto() =
    LoginUserRequestDto(
        username = this.username,
        password = this.password,
    )
