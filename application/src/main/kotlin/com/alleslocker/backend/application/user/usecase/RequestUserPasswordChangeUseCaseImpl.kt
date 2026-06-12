package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.user.dto.request.RequestUserPasswordChangeRequestDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserRole
import java.time.Instant

class RequestUserPasswordChangeUseCaseImpl(
    private val userGateway: UserGateway,
    private val logger: Logger,
) : RequestUserPasswordChangeUseCase {
    override fun execute(
        request: RequestUserPasswordChangeRequestDto,
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

        if (user.mustChangePassword) {
            presenter.presentFailure(ErrorResponse.BadRequest("User $userId were already requested to change"))
            return
        }

        val newUser =
            try {
                userGateway.save(user.copy(mustChangePassword = true))
                return
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user"))
                logger.error("Failed to save user", e)
                return
            }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Requested to change password of ${user.username} with id ${user.id}"),
                performedByUserId = requestorId,
                createdAt = Instant.now(),
            ),
        )
    }
}
