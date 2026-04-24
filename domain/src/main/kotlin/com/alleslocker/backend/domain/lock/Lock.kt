package com.alleslocker.backend.domain.lock

data class Lock (
    val id: LockId,
    val name: LockName,
    val description: LockDescription?,
    // TODO: Maybe add lock types and states

)