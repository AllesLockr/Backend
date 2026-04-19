package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.request.CreatePersonRequestDto
import com.alleslocker.backend.web.person.schema.request.CreatePersonRequestSchema

fun CreatePersonRequestSchema.toDto() = CreatePersonRequestDto(
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email
)