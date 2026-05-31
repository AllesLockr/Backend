package com.alleslocker.backend.persistence.person.specification

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.persistence.person.entity.PersonEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object PersonSpecification {
    fun withFilter(filter: PersonFilterDto): Specification<PersonEntity> =
        Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.search?.trim()?.takeIf { it.isNotBlank() }?.let {
                val term = "%${it.lowercase()}%"
                predicates.add(
                    cb.or(
                        cb.like(cb.lower(root.get("firstname")), term),
                        cb.like(cb.lower(root.get("lastname")), term),
                        cb.like(cb.lower(root.get("email")), term),
                    ),
                )
            }

            cb.and(*predicates.toTypedArray())
        }
}
