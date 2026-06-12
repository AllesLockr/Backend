package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.ChangeUserRoleRequestDto
import com.alleslocker.backend.web.user.schema.request.ChangeUserRoleRequestSchema

fun ChangeUserRoleRequestSchema.toDto(requestorId: String) =
    ChangeUserRoleRequestDto(
        requestorId = requestorId,
        userId = userId,
        role = role.toDto(),
    )
