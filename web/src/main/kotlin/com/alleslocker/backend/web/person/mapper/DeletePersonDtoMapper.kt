package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.request.DeletePersonRequestDto
import com.alleslocker.backend.web.person.schema.request.DeletePersonRequestSchema

fun DeletePersonRequestSchema.toDto() = DeletePersonRequestDto(
    id = this.id
)