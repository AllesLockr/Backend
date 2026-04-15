package eu.luftiger.cae.persistence.user.adapter

import eu.luftiger.cae.application.user.gateway.UserGateway
import eu.luftiger.cae.domain.user.User
import eu.luftiger.cae.domain.user.UserId
import eu.luftiger.cae.persistence.user.entity.UserEntity
import eu.luftiger.cae.persistence.user.mapper.toDomain
import eu.luftiger.cae.persistence.user.mapper.toEntity
import eu.luftiger.cae.persistence.user.repository.UserRepository
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