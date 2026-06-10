package com.alleslocker.backend.persistence.accessgrant.repository

import com.alleslocker.backend.persistence.accessgrant.entity.AccessGrantEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface AccessGrantRepository : JpaRepository<AccessGrantEntity, String> {
    fun findByPersonId(personId: String): List<AccessGrantEntity>

    @Query(
        "SELECT a FROM AccessGrantEntity a " +
            "WHERE (:personId IS NULL OR a.personId = :personId) " +
            "AND (:lockId IS NULL OR a.lockId = :lockId)",
    )
    fun findFiltered(
        @Param("personId") personId: String?,
        @Param("lockId") lockId: String?,
        pageable: Pageable,
    ): Page<AccessGrantEntity>
}
