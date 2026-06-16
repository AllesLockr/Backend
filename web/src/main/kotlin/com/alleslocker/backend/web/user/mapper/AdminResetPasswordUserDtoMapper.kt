package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.AdminResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.AdminResetPasswordUserResponseDto
import com.alleslocker.backend.web.user.schema.request.AdminResetPasswordUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.AdminResetPasswordUserResponseSchema

fun AdminResetPasswordUserRequestSchema.toDto(requestorId: String) =
    AdminResetPasswordUserRequestDto(
        requestorId = requestorId,
        userId = userId,
    )

fun AdminResetPasswordUserResponseDto.toSchema() =
    AdminResetPasswordUserResponseSchema(
        userId = userId,
        password = password,
    )
