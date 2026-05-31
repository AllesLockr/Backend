package com.alleslocker.backend.application.lock.mapper

import com.alleslocker.backend.application.common.dto.ExternalApiIdentityDto
import com.alleslocker.backend.application.lock.dto.LockDto
import com.alleslocker.backend.domain.lock.Lock

fun Lock.toDto() = LockDto(
    id = this.id.value,
    name = this.name.value,
    serialNumber = this.serialNumber.value,
    tagId = this.lockTagId?.value,
    apiIdentity = this.apiIdentity?.let { ExternalApiIdentityDto(api = it.api.name, externalId = it.externalId.value) },
)