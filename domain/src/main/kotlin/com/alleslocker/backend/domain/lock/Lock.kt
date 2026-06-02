package com.alleslocker.backend.domain.lock

import com.alleslocker.backend.domain.vendor.ExternalApiIdentity

data class Lock(
    val id: LockId,
    val name: LockName,
    val serialNumber: LockSerialNumber,
    val lockTagId: LockTagId? = null,
    val apiIdentity: ExternalApiIdentity? = null,
)
