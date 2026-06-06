package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.CreateUserRequestDto
import com.alleslocker.backend.application.user.dto.response.CreateUserResponseDto
import com.alleslocker.backend.web.user.schema.request.CreateUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.CreateUserResponseSchema

fun CreateUserRequestSchema.toDto(requestorId: String) =
    CreateUserRequestDto(
        requestorId = requestorId,
        firstname = firstname,
        lastname = lastname,
        username = username,
        email = email,
    )

fun CreateUserResponseDto.toSchema() =
    CreateUserResponseSchema(
        id = id,
        password = password,
    )
