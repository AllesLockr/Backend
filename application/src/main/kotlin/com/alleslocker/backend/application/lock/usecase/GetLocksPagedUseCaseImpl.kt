package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.dto.request.GetLocksPagedRequestDto
import com.alleslocker.backend.application.lock.dto.response.GetLocksPagedResponseDto
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.mapper.toDto

internal class GetLocksPagedUseCaseImpl(
    private val lockGateway: LockGateway,
) : GetLocksPagedUseCase {
    override fun execute(
        request: GetLocksPagedRequestDto,
        presenter: OutputBoundary<GetLocksPagedResponseDto>,
    ) {
        if (request.page < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Page must be 0 or greater"))
            return
        }

        if (request.size !in 1..500) {
            presenter.presentFailure(ErrorResponse.BadRequest("Size must be between 1 and 500"))
            return
        }

        val page =
            try {
                lockGateway.getAllLocksPaged(
                    page = request.page,
                    size = request.size,
                )
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load locks"))
                return
            }

        presenter.present(
            GetLocksPagedResponseDto(
                page.map { it.toDto() },
            ),
        )
    }
}
