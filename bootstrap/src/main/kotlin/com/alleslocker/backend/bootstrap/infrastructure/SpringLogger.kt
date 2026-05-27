package com.alleslocker.backend.bootstrap.infrastructure

import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.domain.auditlog.AuditLog
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.slf4j.Logger as Slf4jLogger

@Component
class SpringLogger(private val gateway: AuditLogGateway) : Logger {

    private fun log(): Slf4jLogger {
        val callerClassName = StackWalker.getInstance().walk { stream ->
            stream
                .map { it.className }
                .filter { name -> name != SpringLogger::class.java.name && !name.startsWith("kotlin.") }
                .findFirst()
                .orElse(SpringLogger::class.java.name)
        }
        return LoggerFactory.getLogger(callerClassName)
    }

    override fun info(message: String) {
        log().info(message)
    }

    override fun error(message: String) {
        log().error(message)
    }

    override fun audit(auditLog: AuditLog) {
        log().info("[AUDIT] ${auditLog.message}")
        try {
            gateway.save(auditLog)
        } catch (e: Exception) {
            log().error("[AUDIT] ${auditLog.message}", e)
        }
    }
}
