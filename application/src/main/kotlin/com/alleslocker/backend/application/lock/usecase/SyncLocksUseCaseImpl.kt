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
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
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
            } catch (e: Exception) {
                logger.error("Failed to fetch locks from ISEO", e)
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to fetch locks from ISEO"))
                return
            }

        val fetchedSerialNumbers = fetchedLocks.map { it.serialNumber }.toSet()

        val existingBySerial: Map<String, Lock> =
            try {
                lockGateway
                    .findBySerialNumbers(fetchedSerialNumbers.map { LockSerialNumber(it) }.toSet())
                    .associateBy { it.serialNumber.value }
            } catch (e: Exception) {
                logger.error("Failed to load existing locks for upsert", e)
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load existing locks"))
                return
            }

        val locksToUpsert =
            fetchedLocks.map { fetchedLock ->
                val existing = existingBySerial[fetchedLock.serialNumber]
                val lockId = existing?.id ?: LockId.generate()
                val metadata =
                    buildSet {
                        existing?.metadata?.let { addAll(it) }
                        fetchedLock.tagId?.let { tagId ->
                            removeIf { it.key == "tagId" }
                            add(MetadataEntry(key = "tagId", value = tagId.toString()))
                        }
                    }
                Lock(
                    id = lockId,
                    name = LockName(fetchedLock.name),
                    serialNumber = LockSerialNumber(fetchedLock.serialNumber),
                    metadata = metadata,
                    apiIdentity = ExternalApiIdentity(fetchedLock.vendor, ExternalId(fetchedLock.externalId)),
                )
            }

        val allLocalLocks =
            try {
                lockGateway.findAll()
            } catch (e: Exception) {
                logger.error("Failed to load local locks for cleanup", e)
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load local locks for cleanup"))
                return
            }

        val idsToDelete =
            allLocalLocks
                .filter { it.apiIdentity?.api == AvailableVendors.ISEO && it.serialNumber.value !in fetchedSerialNumbers }
                .map { it.id }

        try {
            lockGateway.syncLocks(locksToUpsert, idsToDelete)
        } catch (e: Exception) {
            logger.error("Failed to sync locks to database", e)
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to sync locks to database"))
            return
        }

        runCatching {
            logger.audit(
                AuditLog(
                    id = AuditLogId.generate(),
                    message = AuditLogMessage("Synced locks from ISEO: ${locksToUpsert.size} upserted, ${idsToDelete.size} deleted"),
                    performedByUserId = UserId(request.requesterId),
                    createdAt = Instant.now(),
                ),
            )
        }

        presenter.present(SyncLocksResponseDto(synced = locksToUpsert.size, deleted = idsToDelete.size))
    }
}
