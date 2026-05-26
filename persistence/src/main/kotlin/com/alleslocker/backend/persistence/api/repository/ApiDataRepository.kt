package com.alleslocker.backend.persistence.api.repository

import com.alleslocker.backend.persistence.api.entity.ApiDataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ApiDataRepository : JpaRepository<ApiDataEntity, String> {
}