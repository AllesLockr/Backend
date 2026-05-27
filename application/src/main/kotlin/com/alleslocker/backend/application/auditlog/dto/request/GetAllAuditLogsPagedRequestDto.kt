package com.alleslocker.backend.application.auditlog.dto.request

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto

data class GetAllAuditLogsPagedRequestDto(val page: Int?, val size: Int?, val filter: AuditLogFilterDto?)
