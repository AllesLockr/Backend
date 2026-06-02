package com.alleslocker.backend.application.auditlog.usecase

import com.alleslocker.backend.application.auditlog.dto.filter.AuditLogFilterDto
import com.alleslocker.backend.application.auditlog.dto.request.GetAllAuditLogsPagedRequestDto
import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogResponseDto
import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogsPagedResponseDto
import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.domain.auditlog.AuditLog

class GetAllAuditLogsPagedUseCaseImpl(
    private val gateway: AuditLogGateway,
    private val logger: Logger,
) : GetAllAuditLogsPagedUseCase {
    override fun execute(
        request: GetAllAuditLogsPagedRequestDto,
        presenter: OutputBoundary<GetAuditLogsPagedResponseDto>,
    ) {
        var pageNum = 0
        var pageSize = 10

        if (request.page != null) {
            pageNum = request.page
        }

        if (request.size != null) {
            pageSize = request.size
        }

        if (pageNum < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Page must be 0 or greater"))
            return
        }

        if (pageSize !in 1..500) {
            presenter.presentFailure(ErrorResponse.BadRequest("Size must be between 1 and 500"))
            return
        }

        var filter = AuditLogFilterDto()

        if (request.filter != null) {
            filter = request.filter

            if (filter.toDate != null && filter.toDate.isBefore(filter.fromDate)) {
                presenter.presentFailure(ErrorResponse.BadRequest("fromDate must be before toDate"))
                return
            }
        }

        val page: Page<AuditLog> =
            try {
                gateway.getAllAuditLogsPaged(filter, pageNum, pageSize)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error getting audit logs."))
                logger.error(e.message ?: "Unknown error")
                return
            }

        val pageDto =
            page.map {
                GetAuditLogResponseDto(
                    it.id.value,
                    message = it.message.value,
                    performedByUserId = it.performedByUserId.value,
                    createdAt = it.createdAt.toString(),
                )
            }
        presenter.present(
            GetAuditLogsPagedResponseDto(pageDto),
        )
    }
}
