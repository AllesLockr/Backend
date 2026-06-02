package com.alleslocker.backend.web.user.mapper

import com.alleslocker.backend.application.user.dto.request.GetUsersPagedRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUsersPagedResponseDto
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.web.common.mapper.toSchema
import com.alleslocker.backend.web.user.schema.request.GetUsersPagedRequestSchema
import com.alleslocker.backend.web.user.schema.response.GetUsersPagedResponseSchema

fun GetUsersPagedRequestSchema.toDto(requesterId: String) =
    GetUsersPagedRequestDto(
        requesterId = requesterId,
        filter = filter.toDto(),
        page = page,
        size = size,
    )

fun GetUsersPagedResponseDto.toSchema() =
    GetUsersPagedResponseSchema(
        page = page.toSchema { it.toSchema() },
    )
