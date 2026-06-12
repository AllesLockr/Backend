package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.user.dto.request.EditUserRequestDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import java.time.Instant
import kotlin.math.log

internal class EditUserUseCaseImpl(
    private val userGateway: UserGateway,
    private val logger: Logger,
) : EditUserUseCase {
    override fun execute(
        request: EditUserRequestDto,
        presenter: OutputBoundary<Unit>,
    ) {
        val requestorId =
            try {
                UserId(request.requestorId)
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

        if (request.email != null && request.email != user.email.value) {
            val exists =
                try {
                    userGateway.existsByEmail(request.email)
                } catch (e: Exception) {
                    presenter.presentFailure(ErrorResponse.InternalServerError("Failed to check existense of email"))
                    logger.error("Failed to check existense of email", e)
                    return
                }

            if (exists) {
                presenter.presentFailure(ErrorResponse.AlreadyExists("User with email ${request.email} already exists"))
                return
            }
        }

        if (request.username != null && request.username != user.username.value) {
            val exists =
                try {
                    userGateway.existsByUsername(request.username)
                } catch (e: Exception) {
                    presenter.presentFailure(ErrorResponse.InternalServerError("Failed to check existense of username"))
                    logger.error("Failed to check existense of username", e)
                    return
                }

            if (exists) {
                presenter.presentFailure(
                    ErrorResponse.AlreadyExists("User with username ${request.username} already exists"),
                )
                return
            }
        }

        val userCopy =
            try {
                user.copy(
                    firstname =
                        request.firstname?.let {
                            UserFirstname(it)
                        } ?: user.firstname,
                    lastname =
                        request.lastname?.let {
                            UserLastname(it)
                        } ?: user.lastname,
                    username =
                        request.username?.let {
                            Username(it)
                        } ?: user.username,
                    email =
                        request.email?.let {
                            UserEmail(it)
                        } ?: user.email,
                )
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid arguments: ${e.message}"))
                return
            }

        val saved =
            try {
                userGateway.save(userCopy)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user"))
                logger.error("Failed to save user", e)
                return
            }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message =
                    AuditLogMessage(
                        "Edited user ${saved.username.value} with id ${saved.id.value}",
                    ),
                performedByUserId = requestorId,
                createdAt = Instant.now(),
            ),
        )
    }
}
