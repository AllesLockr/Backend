package com.alleslocker.backend.web.lock.mapper

import com.alleslocker.backend.application.lock.dto.request.UpdateLockRequestDto
import com.alleslocker.backend.web.lock.schema.request.UpdateLockRequestSchema

fun UpdateLockRequestSchema.toDto(id: String) =
    UpdateLockRequestDto(
        id = id,
        name = name,
        serialNumber = serialNumber,
        metadata = metadata,
        apiIdentity = apiIdentity,
    )
