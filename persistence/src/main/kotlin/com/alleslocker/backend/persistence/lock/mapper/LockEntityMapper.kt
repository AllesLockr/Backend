package com.alleslocker.backend.persistence.lock.mapper

import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.api.ExternalId
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.persistence.lock.entity.LockEntity
import com.alleslocker.backend.persistence.shared.mapper.toDomain
import com.alleslocker.backend.persistence.shared.mapper.toEntity

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
        metadata = metadata.map { it.toDomain() }.toSet(),
        apiIdentity = if (api != null && id != null) ExternalApiIdentity(api, ExternalId(id)) else null,
    )
}

fun Lock.toEntity(existing: LockEntity? = null): LockEntity {
    val entity = existing ?: LockEntity()

    entity.id = this.id.value
    entity.name = this.name.value
    entity.serialNumber = this.serialNumber.value
    entity.metadata = this.metadata.map { it.toEntity() }.toMutableSet()
    entity.externalApi = this.apiIdentity?.api
    entity.externalId = this.apiIdentity?.externalId?.value

    return entity
}
