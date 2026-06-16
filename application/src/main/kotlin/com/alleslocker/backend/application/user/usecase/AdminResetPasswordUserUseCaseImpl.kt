package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.common.service.PasswordGeneratorService
import com.alleslocker.backend.application.user.dto.request.AdminResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.AdminResetPasswordUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserRole
import java.time.Instant

internal class AdminResetPasswordUserUseCaseImpl(
    private val userGateway: UserGateway,
    private val passwordHasher: PasswordHasher,
    private val passwordGeneratorService: PasswordGeneratorService,
    private val logger: Logger,
) : AdminResetPasswordUserUseCase {
    override fun execute(
        request: AdminResetPasswordUserRequestDto,
        presenter: OutputBoundary<AdminResetPasswordUserResponseDto>,
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
            presenter.presentFailure(ErrorResponse.NotFound("Requestor not found"))
            return
        }

        if (requestor.role != UserRole.ADMIN) {
            presenter.presentFailure(ErrorResponse.Unauthorized("Only admins can reset passwords"))
            return
        }

        val targetUserId =
            try {
                UserId(request.userId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid target user ID: ${e.message}"))
                return
            }

        val targetUser =
            try {
                userGateway.findById(targetUserId)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error finding target user"))
                logger.error("Failed to load target user", e)
                return
            }
        if (targetUser == null) {
            presenter.presentFailure(ErrorResponse.NotFound("Target user not found"))
            return
        }

        val clearPassword = passwordGeneratorService.generate()
        val passwordHash =
            try {
                PasswordHash(passwordHasher.hash(clearPassword))
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid password hash: ${e.message}"))
                return
            }

        val updatedUser =
            try {
                userGateway.save(
                    targetUser.copy(
                        passwordHash = passwordHash,
                        mustChangePassword = true,
                    ),
                )
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Error saving user"))
                logger.error("Failed to save user", e)
                return
            }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message =
                    AuditLogMessage(
                        "Admin reset password for user ${updatedUser.username.value} with id ${updatedUser.id.value}",
                    ),
                performedByUserId = requestorId,
                createdAt = Instant.now(),
            ),
        )

        presenter.present(
            AdminResetPasswordUserResponseDto(
                userId = updatedUser.id.value,
                password = clearPassword,
            ),
        )
    }
}
