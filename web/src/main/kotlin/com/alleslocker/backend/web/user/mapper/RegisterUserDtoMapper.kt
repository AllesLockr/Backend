package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.RegisterUserRequestDto
import com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto
import com.alleslocker.backend.web.user.schema.request.RegisterUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.RegisterUserResponseSchema

fun RegisterUserRequestSchema.toDto() = RegisterUserRequestDto(
    username = this.username,
    password = this.password
)