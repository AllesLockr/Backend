package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.RequestUserPasswordChangeRequestDto
import com.alleslocker.backend.web.user.schema.request.RequestUserPasswordChangeRequestSchema

fun RequestUserPasswordChangeRequestSchema.toDto(requestorId: String) =
    RequestUserPasswordChangeRequestDto(
        requestorId = requestorId,
        userId = userId,
    )
