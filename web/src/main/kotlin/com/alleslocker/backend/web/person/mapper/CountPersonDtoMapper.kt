package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.response.CountPersonsResponseDto
import com.alleslocker.backend.web.person.schema.response.CountPersonsResponseSchema

fun CountPersonsResponseDto.toSchema() = CountPersonsResponseSchema(
    count = this.count
)