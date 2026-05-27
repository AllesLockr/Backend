package com.alleslocker.backend.domain.lock

data class Lock (
    val id: LockId,
    val apiId: String,
    val name: LockName,
    val serialNumber: LockSerialNumber
)