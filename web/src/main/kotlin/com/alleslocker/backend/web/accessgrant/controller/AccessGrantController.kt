package com.alleslocker.backend.web.accessgrant.controller

import com.alleslocker.backend.application.accessgrant.usecase.GetAccessGrantsPagedUseCase
import com.alleslocker.backend.application.accessgrant.usecase.GrantAccessUseCase
import com.alleslocker.backend.application.accessgrant.usecase.RevokeAccessUseCase
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.web.accessgrant.mapper.toDto
import com.alleslocker.backend.web.accessgrant.presenter.GetAccessGrantsPagedPresenter
import com.alleslocker.backend.web.accessgrant.presenter.GrantAccessPresenter
import com.alleslocker.backend.web.accessgrant.presenter.RevokeAccessPresenter
import com.alleslocker.backend.web.accessgrant.schema.request.GetAccessGrantsPagedRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.request.GrantAccessRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.request.RevokeAccessRequestSchema
import com.alleslocker.backend.web.accessgrant.schema.response.GetAccessGrantsPagedResponseSchema
import com.alleslocker.backend.web.accessgrant.schema.response.GrantAccessResponseSchema
import com.alleslocker.backend.web.accessgrant.schema.response.RevokeAccessResponseSchema
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

@Tag(name = "Access Grant")
@RestController
@RequestMapping("/api/v1/access-grant")
class AccessGrantController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    @Operation(
        summary = "Grant a person permission to open a lock for a time window.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Access granted.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GrantAccessResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Person or lock not found.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Person not provisioned on the lock's vendor.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping()
    fun grantAccess(
        @RequestBody request: GrantAccessRequestSchema,
        @AuthenticationPrincipal requesterId: String,
    ) {
        val presenter = GrantAccessPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GrantAccessUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Revoke a person's permission to open a lock.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Access revoked.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = RevokeAccessResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Access grant not found.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "422",
                description = "Access grant is not present on any vendor.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/revoke")
    fun revokeAccess(
        @RequestBody request: RevokeAccessRequestSchema,
        @AuthenticationPrincipal requesterId: String,
    ) {
        val presenter = RevokeAccessPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(RevokeAccessUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Get access grants paginated, optionally filtered by person or lock.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetAccessGrantsPagedResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid page, size, personId or lockId.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong.",
                content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
            ),
        ],
    )
    @PostMapping("/all")
    fun getAccessGrantsPaged(
        @RequestBody request: GetAccessGrantsPagedRequestSchema,
    ) {
        val presenter = GetAccessGrantsPagedPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetAccessGrantsPagedUseCase::class).execute(request.toDto(), presenter)
    }
}
