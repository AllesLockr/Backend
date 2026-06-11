package com.alleslocker.backend.persistence.accessgrant.adapter

import com.alleslocker.backend.application.accessgrant.gateway.AccessGrantGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.domain.accessgrant.AccessGrant
import com.alleslocker.backend.domain.accessgrant.AccessGrantId
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.persistence.accessgrant.mapper.toDomain
import com.alleslocker.backend.persistence.accessgrant.mapper.toEntity
import com.alleslocker.backend.persistence.accessgrant.repository.AccessGrantRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
open class AccessGrantGatewayAdapter(
    private val repository: AccessGrantRepository,
) : AccessGrantGateway {
    override fun save(entity: AccessGrant): AccessGrant {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: AccessGrantId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: AccessGrantId): AccessGrant? = repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: AccessGrantId): Boolean = repository.existsById(id.value)

    override fun findByPersonId(personId: PersonId): List<AccessGrant> =
        repository.findByPersonId(personId.value).map {
            it.toDomain()
        }

    override fun getAllGrantsPaged(
        personId: PersonId?,
        lockId: LockId?,
        page: Int,
        size: Int,
    ): Page<AccessGrant> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "startAt"))
        val result = repository.findFiltered(personId?.value, lockId?.value, pageable)

        return Page(
            content = result.content.map { it.toDomain() },
            totalElements = result.totalElements,
            size = result.size,
            totalPages = result.totalPages,
            page = page,
        )
    }
}
