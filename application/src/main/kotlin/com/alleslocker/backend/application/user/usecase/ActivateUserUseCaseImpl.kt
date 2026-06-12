package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.user.dto.request.ActivateUserRequestDto
import com.alleslocker.backend.application.user.dto.request.DeactivateUserRequestDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserRole
import java.time.Instant
import kotlin.math.log

class ActivateUserUseCaseImpl(
    val userGateway: UserGateway,
    val logger: Logger,
) : ActivateUserUseCase {
    override fun execute(
        request: ActivateUserRequestDto,
        presenter: OutputBoundary<Unit>,
    ) {
        val requestorId =
            try {
                UserId(request.userId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid id: ${e.message}"))
                return
            }

        val requestorUser =
            try {
                userGateway.findById(requestorId)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load user"))
                logger.error("Failed to load user", e)
                return
            }

        if (requestorUser == null) {
            presenter.presentFailure(ErrorResponse.NotFound("User $requestorId does not exist"))
            return
        }

        if (requestorUser.role != UserRole.ADMIN) {
            presenter.presentFailure(ErrorResponse.Unauthorized("User $requestorId is not admin"))
            return
        }

        val userId =
            try {
                UserId(request.userId)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid id: ${e.message}"))
                return
            }

        val user =
            try {
                userGateway.findById(userId)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load user"))
                logger.error("Failed to load user", e)
                return
            }

        if (user == null) {
            presenter.presentFailure(ErrorResponse.NotFound("User $userId does not exist"))
            return
        }

        if (user.isActive) {
            presenter.presentFailure(ErrorResponse.BadRequest("User $userId is already activated"))
            return
        }

        val saved =
            try {
                userGateway.save(user.copy(isActive = true))
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user ${e.message}"))
                logger.error("Failed to save user", e)
                return
            }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message =
                    AuditLogMessage(
                        "Activated user ${saved.username.value} with id ${saved.id.value}",
                    ),
                performedByUserId = requestorId,
                createdAt = Instant.now(),
            ),
        )
    }
}
