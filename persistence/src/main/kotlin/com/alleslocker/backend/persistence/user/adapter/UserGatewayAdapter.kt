package com.alleslocker.backend.persistence.user.adapter

import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.persistence.user.mapper.toDomain
import com.alleslocker.backend.persistence.user.mapper.toEntity
import com.alleslocker.backend.persistence.user.repository.UserRepository
import org.springframework.stereotype.Component

@Component
class UserGatewayAdapter(
    private val repository: UserRepository
) : UserGateway {

    override fun save(entity: User): User {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: UserId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: UserId): User? =
        repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: UserId): Boolean =
        repository.existsById(id.value)

    override fun findByUsername(username: String): User? =
        repository.findByUsername(username)?.toDomain()
}