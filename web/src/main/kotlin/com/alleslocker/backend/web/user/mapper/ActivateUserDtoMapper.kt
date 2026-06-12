package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.ActivateUserRequestDto
import com.alleslocker.backend.web.user.schema.request.ActivateUserRequestSchema

fun ActivateUserRequestSchema.toDto(requestorId: String) =
    ActivateUserRequestDto(
        requestorId = requestorId,
        userId = userId,
    )
