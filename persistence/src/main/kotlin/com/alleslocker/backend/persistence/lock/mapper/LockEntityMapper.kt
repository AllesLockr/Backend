package com.alleslocker.backend.persistence.lock.mapper

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
    externalIds = this.externalIds.toMap(),
)

fun Lock.toEntity(existing: LockEntity? = null): LockEntity {
    val entity = existing ?: LockEntity()

    entity.id = this.id.value
    entity.name = this.name.value
    entity.serialNumber = this.serialNumber.value
    entity.tagId = this.lockTagId?.value
    entity.externalIds = this.externalIds.toMutableMap()

    return entity
}