package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.EditUserRequestDto
import com.alleslocker.backend.web.user.schema.request.EditUserRequestSchema

fun EditUserRequestSchema.toDto(requestorId: String) =
    EditUserRequestDto(
        requestorId = requestorId,
        userId = userId,
        firstname = firstname,
        lastname = lastname,
        username = username,
        email = email,
    )
