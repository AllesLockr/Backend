package com.alleslocker.backend.application.lock.mapper

import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.lock.LockTagId

fun Lock.toDto() = LockDto(
    id = this.id.value,
    name = this.name.value,
    serialNumber = this.serialNumber.value,
    tagId = this.lockTagId?.value,
    externalIds = this.externalIds,
)

fun LockDto.toDomain() = Lock(
    id = LockId(this.id),
    name = LockName(this.name),
    serialNumber = LockSerialNumber(this.serialNumber),
    lockTagId = this.tagId?.let { LockTagId(it) },
    externalIds = this.externalIds,
)