package com.alleslocker.backend.domain.auditlog

@JvmInline
value class AuditLogMessage(val value: String) {
    init {
        require(value.isNotBlank()) { "AuditLogMessage cannot be blank" }
    }
}
