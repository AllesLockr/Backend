package com.alleslocker.backend.domain.accessgrant

import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity

data class AccessGrant(
    val id: AccessGrantId,
    val personId: PersonId,
    val lockId: LockId,
    val schedule: AccessSchedule,
    val apiIdentity: ExternalApiIdentity? = null,
)
