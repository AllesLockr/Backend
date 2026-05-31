package com.alleslocker.backend.persistence.auditlog.mapper

import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.persistence.auditlog.entity.AuditLogEntity
import com.alleslocker.backend.persistence.user.entity.UserEntity

fun AuditLogEntity.toDomain(): AuditLog =
    AuditLog(
        id = AuditLogId(id),
        message = AuditLogMessage(message),
        performedByUserId = UserId(this.performedBy.id),
        createdAt = createdAt,
    )

fun AuditLog.toEntity(existing: AuditLogEntity? = null): AuditLogEntity {
    val entity = existing ?: AuditLogEntity()

    entity.id = id.value
    entity.message = message.value
    entity.performedBy = UserEntity().apply { id = performedByUserId.value }
    entity.createdAt = createdAt

    return entity
}
