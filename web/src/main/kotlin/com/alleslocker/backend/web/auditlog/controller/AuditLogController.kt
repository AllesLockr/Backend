package com.alleslocker.backend.web.auditlog.controller

import com.alleslocker.backend.application.auditlog.dto.request.GetAllAuditLogsPagedRequestDto
import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogsPagedResponseDto
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCase
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.web.auditlog.presenter.GetAllAuditLogsPagedPresenter
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Audit Logs")
@RestController
@RequestMapping("/api/v1/audit-logs")
class AuditLogController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    @Operation(
        summary = "Get all audit logs paginated.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetAuditLogsPagedResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid page or size.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong...rip",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),

        ],
    )
    @PostMapping("/all")
    fun getAllAuditLogsPaged(
        @RequestBody request: GetAllAuditLogsPagedRequestDto,
    ) {
        val presenter = GetAllAuditLogsPagedPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetAllAuditLogsPagedUseCase::class).execute(request, presenter)
    }
}
