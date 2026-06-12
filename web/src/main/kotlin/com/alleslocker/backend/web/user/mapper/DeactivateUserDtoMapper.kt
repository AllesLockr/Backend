package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.DeactivateUserRequestDto
import com.alleslocker.backend.web.user.schema.request.DeactivateUserRequestSchema

fun DeactivateUserRequestSchema.toDto(requestorId: String) =
    DeactivateUserRequestDto(
        requestorId = requestorId,
        userId = userId,
    )
