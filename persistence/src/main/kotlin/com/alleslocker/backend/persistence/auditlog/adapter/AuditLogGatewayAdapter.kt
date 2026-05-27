package com.alleslocker.backend.persistence.auditlog.adapter

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto
import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.persistence.auditlog.mapper.toDomain
import com.alleslocker.backend.persistence.auditlog.mapper.toEntity
import com.alleslocker.backend.persistence.auditlog.repository.AuditLogRepository
import com.alleslocker.backend.persistence.auditlog.specification.AuditLogSpecification
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class AuditLogGatewayAdapter(private val repository: AuditLogRepository) : AuditLogGateway {
    override fun getAllAuditLogsPaged(
        filter: AuditLogFilterDto,
        page: Int,
        size: Int
    ): Page<AuditLog> {
        val pageable = PageRequest.of(
            page, size, Sort.by(
                Sort.Order.desc("createdAt"),
                Sort.Order.desc("id")
            )
        )
        val specification = AuditLogSpecification.withFilter(filter)
        val result = repository.findAll(specification, pageable)

        return Page(
            content = result.content.map { it.toDomain() },
            totalElements = result.totalElements,
            size = result.size,
            totalPages = result.totalPages,
            page = page
        )
    }

    override fun save(entity: AuditLog): AuditLog {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: AuditLogId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: AuditLogId): AuditLog? {
        return repository.findById(id.value).orElse(null)?.toDomain()
    }

    override fun exists(id: AuditLogId): Boolean {
        return repository.existsById(id.value)
    }
}