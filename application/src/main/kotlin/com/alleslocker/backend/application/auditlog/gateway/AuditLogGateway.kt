package com.alleslocker.backend.application.auditlog.gateway

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto
import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId

interface AuditLogGateway : ReadWriteGateway<AuditLog, AuditLogId> {
    fun getAllAuditLogsPaged(
        filter: AuditLogFilterDto,
        page: Int,
        size: Int,
    ): Page<AuditLog>
}
