package com.alleslocker.backend.persistence.auditlog.specification

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto
import com.alleslocker.backend.persistence.auditlog.entity.AuditLogEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object AuditLogSpecification {
    fun withFilter(filter: AuditLogFilterDto): Specification<AuditLogEntity> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.fromDate?.let {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it))
            }

            filter.toDate?.let {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it))
            }

            filter.performedByUserId?.let { userId ->
                predicates.add(cb.equal(root.get<Any>("performedBy").get<String>("id"), userId))
            }

            cb.and(*predicates.toTypedArray())
        }
}
