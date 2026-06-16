package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.application.lock.dto.request.CreateLockRequestDto
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.mapper.toDto
import com.alleslocker.backend.domain.vendor.AvailableVendors

class CreateLockUseCaseImpl(
    private val lockAdapter: LockAdapter,
    private val lockGateway: LockGateway,
    private val logger: Logger,
) : CreateLockUseCase {
    override fun execute(
        request: CreateLockRequestDto,
        presenter: OutputBoundary<LockDto>,
    ) {
        val forVendor =
            try {
                AvailableVendors.valueOf(request.forVendor)
            } catch (e: IllegalArgumentException) {
                return presenter.presentFailure(
                    ErrorResponse.UnprocessableEntity("${request.forVendor} is not implemented (yet) :("),
                )
            }

        val lock = lockAdapter.createLock(forVendor)

        try {
            lockGateway.save(lock)
        } catch (e: Exception) {
            logger.error("Error creating lock", e)
            return presenter.presentFailure(ErrorResponse.InternalServerError("Error while saving lock!"))
        }

        return presenter.present(lock.toDto())
    }
}
