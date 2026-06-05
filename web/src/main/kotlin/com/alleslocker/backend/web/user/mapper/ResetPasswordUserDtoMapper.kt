package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.ResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto
import com.alleslocker.backend.web.user.schema.request.ResetPasswordUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.ResetPasswordUserResponseSchema

fun ResetPasswordUserRequestSchema.toDto(requestorId: String) =
    ResetPasswordUserRequestDto(
        requestorId = requestorId,
        oldPassword = oldPassword,
        newPassword = newPassword,
    )

fun ResetPasswordUserResponseDto.toSchema(token: String) =
    ResetPasswordUserResponseSchema(
        jwtToken = token,
        userId = userId,
    )
