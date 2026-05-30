package com.alleslocker.backend.persistence.lock.mapper

import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.api.ExternalId
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.lock.LockTagId
import com.alleslocker.backend.persistence.lock.entity.LockEntity

fun LockEntity.toDomain(): Lock = Lock(
    id = LockId(this.id),
    name = LockName(this.name),
    serialNumber = LockSerialNumber(this.serialNumber),
    lockTagId = this.tagId?.let { LockTagId(it) },
    apiIdentity = if (this.externalApi != null && this.externalId != null)
        ExternalApiIdentity(this.externalApi!!, ExternalId(this.externalId!!))
    else null,
)

fun Lock.toEntity(existing: LockEntity? = null): LockEntity {
    val entity = existing ?: LockEntity()

    entity.id = this.id.value
    entity.name = this.name.value
    entity.serialNumber = this.serialNumber.value
    entity.tagId = this.lockTagId?.value
    entity.externalApi = this.apiIdentity?.api
    entity.externalId = this.apiIdentity?.externalId?.value

    return entity
}