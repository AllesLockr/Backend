package com.alleslocker.backend.domain.lock

import com.alleslocker.backend.domain.api.ExternalApiIdentity

data class Lock (
    val id: LockId,
    val name: LockName,
    val serialNumber: LockSerialNumber,
    val lockTagId: LockTagId? = null,
    val apiIdentities: Set<ExternalApiIdentity> = emptySet(),
)