package com.alleslocker.backend.application.auditlog.dto.filter

import java.time.Instant

data class AuditLogFilterDto(
    val fromDate: Instant? = null,
    val toDate: Instant? = null,
    val performedByUserId: String? = null
)
