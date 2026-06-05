package com.alleslocker.backend.persistence.accessgrant.repository

import com.alleslocker.backend.persistence.accessgrant.entity.AccessGrantEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AccessGrantRepository : JpaRepository<AccessGrantEntity, String> {
    fun findByPersonId(personId: String): List<AccessGrantEntity>
}