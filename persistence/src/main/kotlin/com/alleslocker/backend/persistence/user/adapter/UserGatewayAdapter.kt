package com.alleslocker.backend.persistence.user.adapter

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.persistence.person.mapper.toDomain
import com.alleslocker.backend.persistence.person.specification.PersonSpecification
import com.alleslocker.backend.persistence.user.mapper.toDomain
import com.alleslocker.backend.persistence.user.mapper.toEntity
import com.alleslocker.backend.persistence.user.repository.UserRepository
import com.alleslocker.backend.persistence.user.specification.UserSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class UserGatewayAdapter(
    private val repository: UserRepository,
) : UserGateway {
    override fun save(entity: User): User {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: UserId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: UserId): User? = repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: UserId): Boolean = repository.existsById(id.value)

    override fun findByUsername(username: String): User? = repository.findByUsername(username)?.toDomain()

    override fun findByEmail(email: String): User? = repository.findByEmail(email)?.toDomain()

    override fun existsByEmail(email: String): Boolean = repository.existsByEmail(email)

    override fun existsByUsername(username: String): Boolean = repository.existsByUsername(username)

    override fun getAllUsersPaged(
        filter: UserFilterDto,
        page: Int,
        size: Int,
    ): Page<User> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        val specification = UserSpecification.withFilter(filter)
        val result = repository.findAll(specification, pageable)

        return Page(
            content = result.content.map { it.toDomain() },
            totalElements = result.totalElements,
            size = result.size,
            totalPages = result.totalPages,
            page = page,
        )
    }
}
