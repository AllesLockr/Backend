package com.alleslocker.backend.persistence.person.mapper

import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.api.ExternalId
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.person.PersonEmail
import com.alleslocker.backend.domain.person.PersonFirstname
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.person.PersonLastname
import com.alleslocker.backend.persistence.person.entity.PersonEntity
import com.alleslocker.backend.persistence.shared.mapper.toDomain
import com.alleslocker.backend.persistence.shared.mapper.toEntity

fun PersonEntity.toDomain(): Person =
    Person(
        id = PersonId(this.id),
        email = PersonEmail(this.email),
        firstname = PersonFirstname(this.firstname),
        lastname = PersonLastname(this.lastname),
        roles = emptySet(), // TODO: map roles
        metadata = metadata.map { it.toDomain() }.toSet(),
        apiIdentities = this.externalIds.map { (api, id) -> ExternalApiIdentity(api, ExternalId(id)) }.toSet(),
    )

fun Person.toEntity(existing: PersonEntity? = null): PersonEntity {
    val entity = existing ?: PersonEntity()

    entity.id = this.id.value
    entity.email = this.email.value
    entity.firstname = this.firstname.value
    entity.lastname = this.lastname.value
    entity.metadata = this.metadata.map { it.toEntity() }.toMutableSet()
    entity.externalIds = this.apiIdentities.associate { it.api to it.externalId.value }.toMutableMap()

    return entity
}
