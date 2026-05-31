package com.alleslocker.backend.persistence.person.mapper

import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.api.ExternalId
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.person.PersonEmail
import com.alleslocker.backend.domain.person.PersonFirstname
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.person.PersonLastname
import com.alleslocker.backend.persistence.person.entity.PersonEntity

fun PersonEntity.toDomain(): Person =
    Person(
        id = PersonId(this.id),
        email = PersonEmail(this.email),
        firstname = PersonFirstname(this.firstname),
        lastname = PersonLastname(this.lastname),
        roles = emptySet(), // TODO: map roles
        apiIdentities = this.externalIds.map { (api, id) -> ExternalApiIdentity(api, ExternalId(id)) }.toSet(),
    )

fun Person.toEntity(existing: PersonEntity? = null): PersonEntity {
    val entity = existing ?: PersonEntity()

    entity.id = this.id.value
    entity.email = this.email.value
    entity.firstname = this.firstname.value
    entity.lastname = this.lastname.value
    entity.externalIds = this.apiIdentities.associate { it.api to it.externalId.value }.toMutableMap()

    return entity
}
