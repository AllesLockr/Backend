package com.alleslocker.backend.web.lock.mapper

import com.alleslocker.backend.application.lock.dto.request.GetLocksPagedRequestDto
import com.alleslocker.backend.application.lock.dto.response.GetLocksPagedResponseDto
import com.alleslocker.backend.web.common.mapper.toSchema
import com.alleslocker.backend.web.lock.schema.request.GetLocksPagedRequestSchema
import com.alleslocker.backend.web.lock.schema.response.GetLocksPagedResponseSchema

fun GetLocksPagedRequestSchema.toDto() = GetLocksPagedRequestDto(
    page = this.page,
    size = this.size,
)

fun GetLocksPagedResponseDto.toSchema() = GetLocksPagedResponseSchema(
    page = this.page.toSchema { it.toSchema() }
)