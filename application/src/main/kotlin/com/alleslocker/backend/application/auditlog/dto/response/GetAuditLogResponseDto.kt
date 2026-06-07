package com.alleslocker.backend.application.auditlog.dto.response

data class GetAuditLogResponseDto(
    val id: String,
    val message: String,
    val performedByUserId: String,
    val createdAt: Long,
)
