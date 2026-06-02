package com.alleslocker.backend.application.lock.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.request.SyncLocksRequestDto
import com.alleslocker.backend.application.lock.dto.response.SyncLocksResponseDto
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.user.UserId
import java.time.Instant

internal class SyncLocksUseCaseImpl(
    private val lockGateway: LockGateway,
    private val lockAdapter: LockAdapter,
    private val logger: Logger,
) : SyncLocksUseCase {
    override fun execute(
        request: SyncLocksRequestDto,
        presenter: OutputBoundary<SyncLocksResponseDto>,
    ) {
        val fetchedLocks =
            try {
                lockAdapter.fetchAllLocks().locks
            } catch (_: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to fetch locks from ISEO"))
                return
            }

        val fetchedSerialNumbers = fetchedLocks.map { it.serialNumber }.toSet()

        var synced = 0
        for (fetchedLock in fetchedLocks) {
            try {
                val existing = lockGateway.findBySerialNumber(LockSerialNumber(fetchedLock.serialNumber))
                val lockId = existing?.id ?: LockId.generate()
                val metadata = buildSet {
                    fetchedLock.tagId?.let { add(MetadataEntry(key = "tagId", value = it.toString())) }
                }
                lockGateway.save(
                    Lock(
                        id = lockId,
                        name = LockName(fetchedLock.name),
                        serialNumber = LockSerialNumber(fetchedLock.serialNumber),
                        metadata = metadata,
                    ),
                )
                synced++
            } catch (_: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to sync lock '${fetchedLock.serialNumber}'"))
                return
            }
        }

        val allLocalLocks =
            try {
                lockGateway.findAll()
            } catch (_: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load local locks for cleanup"))
                return
            }

        var deleted = 0
        for (localLock in allLocalLocks) {
            if (localLock.serialNumber.value !in fetchedSerialNumbers) {
                try {
                    lockGateway.deleteById(localLock.id)
                    deleted++
                } catch (_: Exception) {
                    presenter.presentFailure(ErrorResponse.InternalServerError("Failed to delete stale lock '${localLock.serialNumber.value}'"))
                    return
                }
            }
        }

        runCatching {
            logger.audit(
                AuditLog(
                    id = AuditLogId.generate(),
                    message = AuditLogMessage("Synced locks from ISEO: $synced upserted, $deleted deleted"),
                    performedByUserId = UserId(request.requesterId),
                    createdAt = Instant.now(),
                ),
            )
        }

        presenter.present(SyncLocksResponseDto(synced = synced, deleted = deleted))
    }
}