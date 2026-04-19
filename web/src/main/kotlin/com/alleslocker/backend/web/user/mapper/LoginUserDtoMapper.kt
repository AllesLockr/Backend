package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.application.user.dto.request.RegisterUserRequestDto
import com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.RegisterUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.RegisterUserResponseSchema

fun LoginUserRequestSchema.toDto() = LoginUserRequestDto(
    username = this.username,
    password = this.password
)