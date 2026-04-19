package com.alleslocker.backend.persistence.person.repository

import com.alleslocker.backend.persistence.person.entity.PersonEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PersonRepository : JpaRepository<PersonEntity, String> {
    fun existsByEmail(email: String): Boolean
}