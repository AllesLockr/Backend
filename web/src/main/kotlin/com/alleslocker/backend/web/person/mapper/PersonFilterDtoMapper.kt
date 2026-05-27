package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.web.person.schema.PersonFilterSchema

fun PersonFilterSchema.toDto() = PersonFilterDto(
    search = this.search,
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
)