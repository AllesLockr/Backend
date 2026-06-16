package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.application.lock.dto.request.UpdateLockRequestDto
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.mapper.toDto
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId

class UpdateLockUseCaseImpl(
    private val lockGateway: LockGateway,
    private val lockAdapter: LockAdapter,
    private val logger: Logger,
) : UpdateLockUseCase {
    override fun execute(
        request: UpdateLockRequestDto,
        presenter: OutputBoundary<LockDto>,
    ) {
        val existing =
            lockGateway.findById(LockId(request.id))
                ?: return presenter.presentFailure(ErrorResponse.NotFound("${request.id} not found!"))

        val lockName = request.name ?: existing.name.value

        val serialNumber = request.serialNumber ?: existing.serialNumber.value

        val metadata = request.metadata?.map { MetadataEntry(it.key, it.value) }?.toSet() ?: existing.metadata

        val apiIdentity =
            request.apiIdentity?.let {
                ExternalApiIdentity(
                    AvailableVendors.valueOf(it.api),
                    ExternalId(it.externalId),
                )
            } ?: existing.apiIdentity

        val updateThis =
            existing.copy(
                name = LockName(lockName),
                serialNumber = LockSerialNumber(serialNumber),
                metadata = metadata,
                apiIdentity = apiIdentity,
            )

        val updated =
            try {
                lockAdapter.updateLock(updateThis)
            } catch (e: Exception) {
                logger.error("Error updating lock in lock-adapter", e)
                return presenter.presentFailure(
                    ErrorResponse.InternalServerError(
                        e.message ?: "The error was so severe that we can't even provide an error message :O",
                    ),
                )
            }

        try {
            lockGateway.save(updated)
        } catch (e: Exception) {
            logger.error("Error creating lock", e)
            return presenter.presentFailure(ErrorResponse.InternalServerError("Error while saving lock!"))
        }

        return presenter.present(updated.toDto())
    }
}
