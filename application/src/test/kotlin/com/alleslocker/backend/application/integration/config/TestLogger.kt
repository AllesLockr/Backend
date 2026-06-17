package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.domain.auditlog.AuditLog

class TestLogger : Logger {
    val auditLogs = mutableListOf<AuditLog>()
    val infoMessages = mutableListOf<String>()
    val errorMessages = mutableListOf<String>()

    fun clear() {
        auditLogs.clear()
        infoMessages.clear()
        errorMessages.clear()
    }

    override fun info(message: String) {
        infoMessages.add(message)
    }

    override fun error(
        message: String,
        throwable: Throwable?,
    ) {
        errorMessages.add(message)
    }

    override fun audit(auditLog: AuditLog) {
        auditLogs.add(auditLog)
    }

    override fun debug(message: String) {}
}
