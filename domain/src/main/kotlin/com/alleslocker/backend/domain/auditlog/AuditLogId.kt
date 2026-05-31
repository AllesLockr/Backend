package com.alleslocker.backend.domain.auditlog

import java.util.UUID

@JvmInline
value class AuditLogId(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "AuditLogId cannot be blank" }
    }

    companion object {
        fun generate(): AuditLogId = AuditLogId(UUID.randomUUID().toString())
    }
}
