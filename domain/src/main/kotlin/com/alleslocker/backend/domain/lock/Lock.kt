package com.alleslocker.backend.domain.lock

import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.shared.MetadataEntry

data class Lock(
    val id: LockId,
    val name: LockName,
    val serialNumber: LockSerialNumber,
    val metadata: Set<MetadataEntry> = emptySet(),
    val apiIdentity: ExternalApiIdentity? = null,
)
