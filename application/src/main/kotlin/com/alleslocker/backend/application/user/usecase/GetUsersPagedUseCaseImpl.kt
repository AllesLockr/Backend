package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.person.dto.response.GetPersonsPagedResponseDto
import com.alleslocker.backend.application.person.mapper.toDto
import com.alleslocker.backend.application.user.dto.request.GetUsersPagedRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUsersPagedResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.mapper.toDto
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserRole

class GetUsersPagedUseCaseImpl(
    private val userGateway: UserGateway,
    private val logger: Logger
) : GetUsersPagedUseCase {
    override fun execute(
        request: GetUsersPagedRequestDto,
        presenter: OutputBoundary<GetUsersPagedResponseDto>
    ) {
        val requester = userGateway.findById(UserId(request.requesterId))
        if (requester == null) {
            presenter.presentFailure(ErrorResponse.Unauthorized("Could not find user with id ${request.requesterId}"))
            return
        }

        if (requester.role != UserRole.ADMIN) {
            presenter.presentFailure(ErrorResponse.Unauthorized("User role ${requester.role} doesn't match required roles"))
            return
        }

        if (request.page < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Page must be 0 or greater"))
            return
        }

        if (request.size !in 1..500) {
            presenter.presentFailure(ErrorResponse.BadRequest("Size must be greater than 0 and less than 500"))
            return
        }

        val page =
            try {
                userGateway.getAllUsersPaged(
                    filter = request.filter,
                    page = request.page,
                    size = request.size,
                )
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load users"))
                logger.error("Failed to load users", e)
                return
            }

        presenter.present(
            GetUsersPagedResponseDto(
                page.map { it.toDto() },
            ),
        )
    }
}