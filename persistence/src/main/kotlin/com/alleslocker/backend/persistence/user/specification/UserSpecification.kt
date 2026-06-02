package com.alleslocker.backend.persistence.user.specification

import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.persistence.user.entity.UserEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object UserSpecification {
    fun withFilter(filter: UserFilterDto): Specification<UserEntity> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.search?.trim()?.takeIf { it.isNotBlank() }?.let {
                val term = "%${it.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("firstname")), term),
                        cb.like(cb.lower(root.get("lastname")), term),
                        cb.like(cb.lower(root.get("email")), term),
                        cb.like(cb.lower(root.get("username")), term),
                    ),
                )
            }

            cb.and(*predicates.toTypedArray())
        }
}
