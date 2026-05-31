package com.alleslocker.backend.persistence.person.repository

import com.alleslocker.backend.persistence.person.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PersonRepository :
    JpaRepository<PersonEntity, String>,
    JpaSpecificationExecutor<PersonEntity> {
    fun existsByEmail(email: String): Boolean
}
