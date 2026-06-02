package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.user.dto.request.GetUserRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.mapper.toDto
import com.alleslocker.backend.domain.user.UserId

internal class GetUserUseCaseImpl(
    private val userGateway: UserGateway,
    private val logger: Logger,
) : GetUserUseCase {
    override fun execute(
        request: GetUserRequestDto,
        presenter: OutputBoundary<GetUserResponseDto>,
    ) {
        val id =
            try {
                UserId(request.id)
            } catch (e: IllegalArgumentException) {
                presenter.presentFailure(ErrorResponse.BadRequest("Invalid id: ${e.message}"))
                return
            }

        val user =
            try {
                userGateway.findById(id)
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load user"))
                logger.error("Failed to load user", e)
                return
            }

        if (user == null) {
            presenter.presentFailure(ErrorResponse.NotFound("User not found"))
            return
        }

        presenter.present(
            GetUserResponseDto(
                user.toDto(),
            ),
        )
    }
}
