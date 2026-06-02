package com.alleslocker.backend.persistence.lock.mapper

import com.alleslocker.backend.domain.lock.*
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
import com.alleslocker.backend.persistence.lock.entity.LockEntity

fun LockEntity.toDomain(): Lock {
    val api = externalApi
    val id = externalId
    check((api == null) == (id == null)) {
        "Invalid lock external identity state: externalApi/externalId must both be set or both be null"
    }
    return Lock(
        id = LockId(this.id),
        name = LockName(this.name),
        serialNumber = LockSerialNumber(this.serialNumber),
        lockTagId = this.tagId?.let { LockTagId(it) },
        apiIdentity = if (api != null && id != null) ExternalApiIdentity(api, ExternalId(id)) else null,
    )
}

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
