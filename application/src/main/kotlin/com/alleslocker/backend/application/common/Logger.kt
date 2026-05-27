package com.alleslocker.backend.application.common

import com.alleslocker.backend.domain.auditlog.AuditLog

interface Logger {
    fun info(message: String)
    fun error(message: String)
    fun audit(auditLog: AuditLog)
}