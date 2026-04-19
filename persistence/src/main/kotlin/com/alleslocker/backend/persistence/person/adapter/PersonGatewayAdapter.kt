package com.alleslocker.backend.persistence.person.adapter

import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.persistence.person.mapper.toDomain
import com.alleslocker.backend.persistence.person.mapper.toEntity
import com.alleslocker.backend.persistence.person.repository.PersonRepository
import org.springframework.stereotype.Component

@Component
class PersonGatewayAdapter(
    private val repository: PersonRepository
) : PersonGateway {
    override fun existsByEmail(email: String): Boolean =
        repository.existsByEmail(email)
    
    override fun save(entity: Person): Person {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: PersonId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: PersonId): Person? =
        repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: PersonId): Boolean =
        repository.existsById(id.value)
}