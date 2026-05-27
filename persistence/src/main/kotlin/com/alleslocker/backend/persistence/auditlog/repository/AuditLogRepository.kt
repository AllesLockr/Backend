package com.alleslocker.backend.persistence.auditlog.repository

import com.alleslocker.backend.persistence.auditlog.entity.AuditLogEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AuditLogRepository : JpaRepository<AuditLogEntity, String>, JpaSpecificationExecutor<AuditLogEntity> {
}