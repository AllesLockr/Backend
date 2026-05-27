package com.alleslocker.backend.application.auditlog.usecase

import com.alleslocker.backend.application.auditlog.dto.request.GetAllAuditLogsPagedRequestDto
import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogResponseDto
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.model.Page

interface GetAllAuditLogsPagedUseCase : InputBoundary<GetAllAuditLogsPagedRequestDto, Page<GetAuditLogResponseDto>> {
}