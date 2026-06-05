package com.alleslocker.backend.web.user.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.user.usecase.GetUserUseCase
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCase
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.user.mapper.toDto
import com.alleslocker.backend.web.user.presenter.GetUserPresenter
import com.alleslocker.backend.web.user.presenter.GetUsersPagedPresenter
import com.alleslocker.backend.web.user.presenter.LoginUserPresenter
import com.alleslocker.backend.web.user.presenter.ResetPasswordUserPresenter
import com.alleslocker.backend.web.user.schema.request.GetUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.GetUsersPagedRequestSchema
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.ResetPasswordUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.GetUserResponseSchema
import com.alleslocker.backend.web.user.schema.response.GetUsersPagedResponseSchema
import com.alleslocker.backend.web.user.schema.response.LoginUserResponseSchema
import com.alleslocker.backend.web.user.schema.response.ResetPasswordUserResponseSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService,
) {
    @Operation(
        summary = "Login a user.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginUserResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "The username was not found.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid username or password.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @PostMapping("/auth/login")
    fun login(
        @RequestBody request: LoginUserRequestSchema,
    ) {
        val presenter = LoginUserPresenter(httpServletResponse, jacksonConverter, jwtService)
        useCaseFactory.make(LoginUserUseCase::class).execute(request.toDto(), presenter)
    }

    @Operation(
        summary = "Get all users paginated.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetUsersPagedResponseSchema::class),
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
    fun getUsersPaged(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: GetUsersPagedRequestSchema,
    ) {
        val presenter = GetUsersPagedPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetUsersPagedUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Get a user by id.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetUserResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid id.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found.",
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
    @GetMapping("/{id}")
    fun getUser(
        @PathVariable id: String,
    ) {
        val request = GetUserRequestSchema(id)
        val presenter = GetUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetUserUseCase::class).execute(request.toDto(), presenter)
    }

    @Operation(
        summary = "Reset the authenticated user's password.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ResetPasswordUserResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Wrong old password.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid reset-password request.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "User not found.",
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
    @PostMapping("/reset-password")
    fun resetPassword(
        @AuthenticationPrincipal requestorId: String,
        @RequestBody request: ResetPasswordUserRequestSchema,
    ) {
        val presenter = ResetPasswordUserPresenter(httpServletResponse, jacksonConverter, jwtService)
        useCaseFactory.make(ResetPasswordUserUseCase::class).execute(request.toDto(requestorId), presenter)
    }
}
