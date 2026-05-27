package com.alleslocker.backend.web.person.mapper

import com.alleslocker.backend.application.person.dto.PersonDto
import com.alleslocker.backend.web.person.schema.PersonSchema

fun PersonDto.toSchema() = PersonSchema(
    id = this.id,
    firstname = this.firstname,
    lastname = this.lastname,
    email = this.email,
)