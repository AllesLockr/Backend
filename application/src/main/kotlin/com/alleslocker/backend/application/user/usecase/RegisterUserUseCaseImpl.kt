package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.RegisterUserRequestDto
import com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.Username

internal class RegisterUserUseCaseImpl(
    private val passwordHasher: PasswordHasher,
    private val userGateway: UserGateway
) : RegisterUserUseCase {

    override fun execute(
        request: RegisterUserRequestDto,
        presenter: OutputBoundary<RegisterUserResponseDto>
    ) {
        if (userGateway.findByUsername(request.username) != null) {
            presenter.presentFailure(
                ErrorResponse.AlreadyExists(
                    "User with username: ${request.username} already exists."
                )
            )
            return
        }

        val hashedPassword = passwordHasher.hash(request.password)
        val user = try {
            User(
                id = UserId.generate(),
                username = Username(request.username),
                passwordHash = PasswordHash(hashedPassword)
            )
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.BadRequest(e.message ?: "Invalid user data"))
            return
        }

        val save = try {
            userGateway.save(user)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user: ${e.message}"))
            return
        }

        presenter.present(RegisterUserResponseDto(save.id.value))
    }
}