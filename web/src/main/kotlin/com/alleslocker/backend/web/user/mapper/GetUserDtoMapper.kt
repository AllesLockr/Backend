package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.GetUserRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUserResponseDto
import com.alleslocker.backend.web.user.schema.request.GetUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.GetUserResponseSchema

fun GetUserResponseDto.toSchema() =
    GetUserResponseSchema(
        user.toSchema(),
    )

fun GetUserRequestSchema.toDto() =
    GetUserRequestDto(
        id,
    )
