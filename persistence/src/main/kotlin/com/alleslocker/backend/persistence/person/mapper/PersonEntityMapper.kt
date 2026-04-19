package com.alleslocker.backend.persistence.person.mapper

import com.alleslocker.backend.domain.person.*
import com.alleslocker.backend.persistence.person.entity.PersonEntity

fun PersonEntity.toDomain(): Person = Person(
    id = PersonId(this.id),
    email = PersonEmail(this.email),
    firstname = PersonFirstname(this.firstname),
    lastname = PersonLastname(this.lastname),
    roles = emptySet() // TODO: map roles
)

fun Person.toEntity(existing: PersonEntity? = null): PersonEntity {
    val entity = existing ?: PersonEntity()

    entity.id = this.id.value
    entity.email = this.email.value
    entity.firstname = this.firstname.value
    entity.lastname = this.lastname.value

    return entity
}