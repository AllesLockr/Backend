package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.common.service.PasswordGeneratorService
import com.alleslocker.backend.application.user.dto.request.CreateUserRequestDto
import com.alleslocker.backend.application.user.dto.response.CreateUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username

class CreateUserUseCaseImpl(
    private val userGateway: UserGateway,
    private val logger: Logger,
    private val passwordGeneratorService: PasswordGeneratorService,
    private val passwordHasher: PasswordHasher,
) : CreateUserUseCase {
    override fun execute(
        request: CreateUserRequestDto,
        presenter: OutputBoundary<CreateUserResponseDto>,
    ) {
        val requestorId =
            try {
                UserId(request.requestorId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid requestor ID"))
                return
            }

        val requestor =
            try {
                userGateway.findById(requestorId)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error finding requestor"))
                logger.error("Failed to load requestor", e)
                return
            }
        if (requestor == null) {
            presenter.presentFailure(ErrorResponse.NotFound("Error finding requestor"))
            return
        }

        if (requestor.role != UserRole.ADMIN) {
            presenter.presentFailure(ErrorResponse.Unauthorized("Only admins can create users"))
            return
        }

        val lastname =
            try {
                UserLastname(request.lastname)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid lastname: ${e.message}"))
                return
            }

        val firstname =
            try {
                UserFirstname(request.firstname)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid firstname: ${e.message}"))
                return
            }

        val email =
            try {
                UserEmail(request.email)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid email: ${e.message}"))
                return
            }

        val username =
            try {
                Username(request.username)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid username: ${e.message}"))
                return
            }

        val existingByUsername =
            try {
                userGateway.findByUsername(username.value)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error checking username"))
                logger.error("Failed to check username uniqueness", e)
                return
            }

        if (existingByUsername != null) {
            presenter.presentFailure(ErrorResponse.AlreadyExists("username already exists"))
            return
        }

        val existingByEmail =
            try {
                userGateway.findByEmail(email.value)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error checking email"))
                logger.error("Failed to check email uniqueness", e)
                return
            }

        if (existingByEmail != null) {
            presenter.presentFailure(ErrorResponse.AlreadyExists("email already exists"))
            return
        }

        val password = passwordHasher.hash(passwordGeneratorService.generate())
        val passwordHash =
            try {
                PasswordHash(password)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid password hash: ${e.message}"))
                return
            }

        val user =
            User(
                id = UserId.generate(),
                role = UserRole.USER,
                firstname = firstname,
                lastname = lastname,
                username = username,
                email = email,
                passwordHash = passwordHash,
                isActive = true,
                mustChangePassword = true,
            )
        val saved =
            try {
                userGateway.save(user)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error saving user"))
                logger.error("Failed to save user", e)
                return
            }

        presenter.present(
            CreateUserResponseDto(
                id = saved.id.value,
            ),
        )
    }
}
