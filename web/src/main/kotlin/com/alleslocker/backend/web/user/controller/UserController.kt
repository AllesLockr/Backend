package com.alleslocker.backend.web.user.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.user.usecase.ActivateUserUseCase
import com.alleslocker.backend.application.user.usecase.AdminResetPasswordUserUseCase
import com.alleslocker.backend.application.user.usecase.ChangeUserRoleUseCase
import com.alleslocker.backend.application.user.usecase.CreateUserUseCase
import com.alleslocker.backend.application.user.usecase.DeactivateUserUseCase
import com.alleslocker.backend.application.user.usecase.EditUserUseCase
import com.alleslocker.backend.application.user.usecase.GetUserUseCase
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.RequestUserPasswordChangeUseCase
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCase
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.user.mapper.toDto
import com.alleslocker.backend.web.user.presenter.ActivateUserPresenter
import com.alleslocker.backend.web.user.presenter.AdminResetPasswordUserPresenter
import com.alleslocker.backend.web.user.presenter.ChangeUserRolePresenter
import com.alleslocker.backend.web.user.presenter.CreateUserPresenter
import com.alleslocker.backend.web.user.presenter.DeactivateUserPresenter
import com.alleslocker.backend.web.user.presenter.EditUserPresenter
import com.alleslocker.backend.web.user.presenter.GetUserPresenter
import com.alleslocker.backend.web.user.presenter.GetUsersPagedPresenter
import com.alleslocker.backend.web.user.presenter.LoginUserPresenter
import com.alleslocker.backend.web.user.presenter.RequestUserPasswordChangePresenter
import com.alleslocker.backend.web.user.presenter.ResetPasswordUserPresenter
import com.alleslocker.backend.web.user.schema.request.ActivateUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.AdminResetPasswordUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.ChangeUserRoleRequestSchema
import com.alleslocker.backend.web.user.schema.request.CreateUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.DeactivateUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.EditUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.GetUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.GetUsersPagedRequestSchema
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.RequestUserPasswordChangeRequestSchema
import com.alleslocker.backend.web.user.schema.request.ResetPasswordUserRequestSchema
import com.alleslocker.backend.web.user.schema.response.AdminResetPasswordUserResponseSchema
import com.alleslocker.backend.web.user.schema.response.CreateUserResponseSchema
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

    @Operation(
        summary = "Create a new user (admin only).",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "User successfully created.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = CreateUserResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid request (e.g. invalid requestor ID, lastname, firstname, email or username).",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor not found.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "409",
                description = "Username or email already exists.",
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
    @PostMapping("/create")
    fun createUser(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: CreateUserRequestSchema,
    ) {
        val presenter = CreateUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(CreateUserUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Request a password change for a user (admin only).",
        responses = [
            ApiResponse(responseCode = "200", description = "Password change successfully requested."),
            ApiResponse(
                responseCode = "400",
                description = "Invalid requestor or target user ID.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found.",
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
    @PostMapping("/request-password-change")
    fun requestPasswordChange(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: RequestUserPasswordChangeRequestSchema,
    ) {
        val presenter = RequestUserPasswordChangePresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(RequestUserPasswordChangeUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Activate a user (admin only).",
        responses = [
            ApiResponse(responseCode = "200", description = "User successfully activated."),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID, or user is already active.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found.",
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
    @PostMapping("/activate-user")
    fun activateUser(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: ActivateUserRequestSchema,
    ) {
        val presenter = ActivateUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(ActivateUserUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Deactivate a user (admin only).",
        responses = [
            ApiResponse(responseCode = "200", description = "User successfully deactivated."),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID, or user is already inactive.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found.",
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
    @PostMapping("/deactivate-user")
    fun deactivateUser(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: DeactivateUserRequestSchema,
    ) {
        val presenter = DeactivateUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(DeactivateUserUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Edit a user's details (admin only).",
        responses = [
            ApiResponse(responseCode = "200", description = "User successfully edited."),
            ApiResponse(
                responseCode = "400",
                description = "Invalid ID, firstname, lastname, username or email.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found, or email/username already exists.",
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
    @PostMapping("/edit-user")
    fun editUser(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: EditUserRequestSchema,
    ) {
        val presenter = EditUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(EditUserUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Change a user's role (admin only).",
        responses = [
            ApiResponse(responseCode = "200", description = "User role successfully changed."),
            ApiResponse(
                responseCode = "400",
                description = "Invalid requestor or target user ID.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found.",
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
    @PostMapping("/change-role")
    fun changeRole(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: ChangeUserRoleRequestSchema,
    ) {
        val presenter = ChangeUserRolePresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(ChangeUserRoleUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Admin-reset a user's password (admin only). Generates a new password and returns it.",
        responses = [
            ApiResponse(
                responseCode = "201",
                description = "Password successfully reset.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = AdminResetPasswordUserResponseSchema::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid requestor or target user ID.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "401",
                description = "Requestor is not an admin.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                description = "Requestor or target user not found.",
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
    @PostMapping("/admin-reset-password")
    fun adminResetPassword(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: AdminResetPasswordUserRequestSchema,
    ) {
        val presenter = AdminResetPasswordUserPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(AdminResetPasswordUserUseCase::class).execute(request.toDto(requesterId), presenter)
    }
}
