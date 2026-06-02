package com.alleslocker.backend.persistence.lock.adapter

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.persistence.lock.mapper.toDomain
import com.alleslocker.backend.persistence.lock.mapper.toEntity
import com.alleslocker.backend.persistence.lock.repository.LockRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
open class LockGatewayAdapter(
    private val repository: LockRepository,
) : LockGateway {
    override fun getAllLocksPaged(
        page: Int,
        size: Int,
    ): Page<Lock> {
        val pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"))
        val result = repository.findAll(pageable)

        return Page(
            content = result.content.map { it.toDomain() },
            totalElements = result.totalElements,
            size = result.size,
            totalPages = result.totalPages,
            page = page,
        )
    }

    override fun save(entity: Lock): Lock {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: LockId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: LockId): Lock? = repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: LockId): Boolean = repository.existsById(id.value)

    override fun findBySerialNumber(serialNumber: LockSerialNumber): Lock? = repository.findBySerialNumber(serialNumber.value)?.toDomain()

    override fun findBySerialNumbers(serialNumbers: Set<LockSerialNumber>): List<Lock> {
        if (serialNumbers.isEmpty()) return emptyList()
        return repository.findBySerialNumberIn(serialNumbers.map { it.value }).map { it.toDomain() }
    }

    override fun findAll(): List<Lock> = repository.findAll().map { it.toDomain() }

    @Transactional
    open override fun syncLocks(
        toUpsert: List<Lock>,
        toDeleteIds: List<LockId>,
    ) {
        toUpsert.forEach { lock ->
            val existing = repository.findById(lock.id.value).orElse(null)
            repository.save(lock.toEntity(existing))
        }
        toDeleteIds.forEach { repository.deleteById(it.value) }
    }
}
