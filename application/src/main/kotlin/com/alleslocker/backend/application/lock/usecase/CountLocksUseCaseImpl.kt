package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.dto.request.CountLocksRequestDto
import com.alleslocker.backend.application.lock.dto.response.CountLocksResponseDto
import com.alleslocker.backend.application.lock.gateway.LockGateway

internal class CountLocksUseCaseImpl(
    private val lockGateway: LockGateway,
    private val logger: Logger,
) : CountLocksUseCase {
    override fun execute(
        request: CountLocksRequestDto,
        presenter: OutputBoundary<CountLocksResponseDto>,
    ) {
        val count =
            try {
                lockGateway.count()
            } catch (e: Exception) {
                presenter.presentFailure(
                    ErrorResponse.InternalServerError("Failed to count locks."),
                )
                logger.error("Failed to count locks: ", e)
                return
            }

        presenter.present(CountLocksResponseDto(count))
    }
}
