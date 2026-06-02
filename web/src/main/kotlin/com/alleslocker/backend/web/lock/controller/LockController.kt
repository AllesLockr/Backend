package com.alleslocker.backend.web.lock.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.lock.dto.request.SyncLocksRequestDto
import com.alleslocker.backend.application.lock.usecase.GetLocksPagedUseCase
import com.alleslocker.backend.application.lock.usecase.SyncLocksUseCase
import com.alleslocker.backend.web.lock.mapper.toDto
import com.alleslocker.backend.web.lock.presenter.GetLocksPagedPresenter
import com.alleslocker.backend.web.lock.presenter.SyncLocksPresenter
import com.alleslocker.backend.web.lock.schema.request.GetLocksPagedRequestSchema
import com.alleslocker.backend.web.lock.schema.response.GetLocksPagedResponseSchema
import com.alleslocker.backend.web.lock.schema.response.SyncLocksResponseSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Lock")
@RestController
@RequestMapping("/api/v1/lock")
class LockController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    @Operation(
        summary = "Get all locks paginated.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetLocksPagedResponseSchema::class),
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
    fun getLocksPaged(
        @RequestBody request: GetLocksPagedRequestSchema,
    ) {
        val presenter = GetLocksPagedPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetLocksPagedUseCase::class).execute(request.toDto(), presenter)
    }

    @Operation(
        summary = "Sync locks from ISEO. Creates/updates local locks and removes those no longer in ISEO.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Sync completed successfully.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = SyncLocksResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @PostMapping("/sync")
    fun syncLocks(
        @AuthenticationPrincipal requesterId: String,
    ) {
        val presenter = SyncLocksPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(SyncLocksUseCase::class).execute(SyncLocksRequestDto(requesterId), presenter)
    }
}
