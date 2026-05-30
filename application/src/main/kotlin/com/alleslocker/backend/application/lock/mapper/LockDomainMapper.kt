package com.alleslocker.backend.application.lock.mapper

import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.domain.lock.Lock

fun Lock.toDto() = LockDto(
    id = this.id.value,
    name = this.name.value,
    serialNumber = this.serialNumber.value,
    tagId = this.lockTagId?.value,
    externalApi = this.apiIdentity?.api?.name,
    externalId = this.apiIdentity?.externalId?.value,
)