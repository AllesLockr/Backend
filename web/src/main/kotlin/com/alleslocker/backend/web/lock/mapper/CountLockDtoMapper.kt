package com.alleslocker.backend.web.lock.mapper

import com.alleslocker.backend.application.lock.dto.response.CountLocksResponseDto
import com.alleslocker.backend.web.lock.schema.response.CountLocksResponseSchema

fun CountLocksResponseDto.toSchema() =
    CountLocksResponseSchema(
        count = this.count,
    )
