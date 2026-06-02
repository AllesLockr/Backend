package com.alleslocker.backend.domain.lock

import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity

data class Lock(
    val id: LockId,
    val name: LockName,
    val serialNumber: LockSerialNumber,
    val metadata: Set<MetadataEntry> = emptySet(),
    val apiIdentity: ExternalApiIdentity? = null,
)
