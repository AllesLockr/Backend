package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.ResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.UserId

class ResetPasswordUserUseCaseImpl(
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
    private val userGateway: UserGateway,
) : ResetPasswordUserUseCase {
    override fun execute(
        request: ResetPasswordUserRequestDto,
        presenter: OutputBoundary<ResetPasswordUserResponseDto>
    ) {
        val userId = try {
            UserId(request.requestorId)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid user id: ${e.message}"))
            return
        }

        val user = try {
            userGateway.findById(userId)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load user"))
            logger.error("Failed to load user with id ${request.requestorId}", e)
            return
        }
        if (user == null) {
            presenter.presentFailure(ErrorResponse.NotFound("User doesn't exist"))
            return
        }

        if (!passwordHasher.verify(request.oldPassword, user.passwordHash.value)) {
            presenter.presentFailure(ErrorResponse.Unauthorized("Invalid old password"))
            return
        }

        val newPasswordHashString = passwordHasher.hash(request.newPassword)
        val newPasswordHash = try {
            PasswordHash(newPasswordHashString)
        } catch (e: IllegalArgumentException) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid new password hash: ${e.message}"))
            return
        }

        try {
            userGateway.save(
                user.copy(passwordHash = newPasswordHash)
            )
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user"))
            logger.error("Failed to save user with id ${request.requestorId}", e)
            return
        }

        presenter.present(ResetPasswordUserResponseDto())
    }
}