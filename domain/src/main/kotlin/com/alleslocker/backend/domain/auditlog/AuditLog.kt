package com.alleslocker.backend.domain.auditlog

import com.alleslocker.backend.domain.user.UserId
import java.time.Instant

data class AuditLog(
    val id: AuditLogId,
    val message: AuditLogMessage,
    val performedByUserId: UserId,
    val createdAt: Instant,
)
