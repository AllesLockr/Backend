package com.alleslocker.backend.application.person.mapper

import com.alleslocker.backend.application.person.dto.PersonDto
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.person.PersonEmail
import com.alleslocker.backend.domain.person.PersonFirstname
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.person.PersonLastname

fun Person.toDto() = PersonDto(
    id = this.id.value,
    firstname = this.firstname.value,
    lastname = this.lastname.value,
    email = this.email.value
)

fun PersonDto.toDomain() = Person(
    id = PersonId(this.id),
    firstname = PersonFirstname(this.firstname),
    lastname = PersonLastname(this.lastname),
    email = PersonEmail(this.email),
    roles = emptySet() //TODO: Add roles
)