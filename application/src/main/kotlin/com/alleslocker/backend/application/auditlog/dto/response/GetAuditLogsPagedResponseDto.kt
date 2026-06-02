package com.alleslocker.backend.application.auditlog.dto.response

import com.alleslocker.backend.application.common.model.Page

data class GetAuditLogsPagedResponseDto(
    val page: Page<GetAuditLogResponseDto>,
)
