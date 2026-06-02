package com.alleslocker.backend.web.user.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.person.usecase.GetPersonsPagedUseCase
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCase
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.person.mapper.toDto
import com.alleslocker.backend.web.person.presenter.GetPersonsPagedPresenter
import com.alleslocker.backend.web.person.schema.request.GetPersonsPagedRequestSchema
import com.alleslocker.backend.web.person.schema.response.GetPersonsPagedResponseSchema
import com.alleslocker.backend.web.user.mapper.toDto
import com.alleslocker.backend.web.user.presenter.GetUsersPagedPresenter
import com.alleslocker.backend.web.user.presenter.LoginUserPresenter
import com.alleslocker.backend.web.user.presenter.RegisterUserPresenter
import com.alleslocker.backend.web.user.schema.request.GetUsersPagedRequestSchema
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.RegisterUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.GetUsersPagedResponseSchema
import com.alleslocker.backend.web.user.schema.response.LoginUserResponseSchema
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

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService,
) {
    @PostMapping("/auth/register")
    fun register(
        @RequestBody request: RegisterUserRequestSchema,
    ) {
        val presenter = RegisterUserPresenter(httpServletResponse, jacksonConverter, jwtService)
        useCaseFactory.make(RegisterUserUseCase::class).execute(request.toDto(), presenter)
    }

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
                responseCode = "401",
                description = "Requestor ist unauthorized.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
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
}
