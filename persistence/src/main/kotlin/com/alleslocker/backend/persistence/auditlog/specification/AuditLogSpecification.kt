package com.alleslocker.backend.persistence.auditlog.specification

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto
import com.alleslocker.backend.persistence.auditlog.entity.AuditLogEntity
import com.alleslocker.backend.persistence.user.entity.UserEntity
import jakarta.persistence.criteria.JoinType
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object AuditLogSpecification {
    fun withFilter(filter: AuditLogFilterDto): Specification<AuditLogEntity> =
        Specification { root, query, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.fromDate?.let {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdAt"), it))
            }

            filter.toDate?.let {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdAt"), it))
            }

            filter.searchText
                ?.takeIf { it.isNotBlank() }
                ?.let { searchText ->
                    val isCountQuery =
                        query?.resultType == java.lang.Long::class.java ||
                            query?.resultType == Long::class.javaPrimitiveType

                    if (!isCountQuery) {
                        query?.distinct(true)
                    }

                    val pattern = "%${searchText.lowercase()}%"
                    val performedBy = root.join<AuditLogEntity, UserEntity>("performedBy", JoinType.LEFT)

                    predicates.add(
                        cb.or(
                            cb.like(cb.lower(root.get("message")), pattern),
                            cb.like(cb.lower(performedBy.get("username")), pattern),
                            cb.like(cb.lower(performedBy.get("firstname")), pattern),
                            cb.like(cb.lower(performedBy.get("lastname")), pattern),
                        ),
                    )
                }

            cb.and(*predicates.toTypedArray())
        }
}
