package com.alleslocker.backend.persistence.person.specification

import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.persistence.person.entity.PersonEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

object PersonSpecification {

    fun withFilter(filter: PersonFilterDto): Specification<PersonEntity> {
        return Specification { root, _, cb ->
            val predicates = mutableListOf<Predicate>()

            filter.firstname?.let {
                predicates.add(cb.like(cb.lower(root.get("firstname")), "%${it.lowercase()}%"))
            }
            filter.lastname?.let {
                predicates.add(cb.like(cb.lower(root.get("lastname")), "%${it.lowercase()}%"))
            }
            filter.email?.let {
                predicates.add(cb.like(cb.lower(root.get("email")), "%${it.lowercase()}%"))
            }

            cb.and(*predicates.toTypedArray())
        }
    }
}
