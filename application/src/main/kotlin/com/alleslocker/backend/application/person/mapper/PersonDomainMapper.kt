package com.alleslocker.backend.application.person.mapper

import com.alleslocker.backend.application.person.dto.PersonDto
import com.alleslocker.backend.domain.person.Person

fun Person.toDto() = PersonDto(
    id = this.id.value,
    firstname = this.firstname.value,
    lastname = this.lastname.value,
    email = this.email.value,
    externalIds = this.apiIdentities.associate { it.api.name to it.externalId.value },
)