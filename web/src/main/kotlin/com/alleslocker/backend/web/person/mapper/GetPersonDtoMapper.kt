package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.request.GetPersonRequestDto
import com.alleslocker.backend.application.person.dto.response.GetPersonResponseDto
import com.alleslocker.backend.web.person.schema.request.GetPersonRequestSchema
import com.alleslocker.backend.web.person.schema.response.GetPersonResponseSchema

fun GetPersonRequestSchema.toDto() =
    GetPersonRequestDto(
        personId = personId,
    )

fun GetPersonResponseDto.toSchema() =
    GetPersonResponseSchema(
        person = person.toSchema(),
    )
