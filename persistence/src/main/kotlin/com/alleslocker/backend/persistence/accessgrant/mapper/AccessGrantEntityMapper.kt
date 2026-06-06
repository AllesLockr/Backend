package com.alleslocker.backend.persistence.accessgrant.mapper

import com.alleslocker.backend.domain.accessgrant.AccessGrant
import com.alleslocker.backend.domain.accessgrant.AccessGrantId
import com.alleslocker.backend.domain.accessgrant.AccessSchedule
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.person.PersonId
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
import com.alleslocker.backend.persistence.accessgrant.entity.AccessGrantEntity

fun AccessGrantEntity.toDomain(): AccessGrant {
    val api = externalApi
    val extId = externalId
    return AccessGrant(
        id = AccessGrantId(this.id),
        personId = PersonId(this.personId),
        lockId = LockId(this.lockId),
        schedule = AccessSchedule(start = this.startAt, end = this.endAt),
        apiIdentity = if (api != null && extId != null) ExternalApiIdentity(api, ExternalId(extId)) else null,
    )
}

fun AccessGrant.toEntity(existing: AccessGrantEntity? = null): AccessGrantEntity {
    val entity = existing ?: AccessGrantEntity()

    entity.id = this.id.value
    entity.personId = this.personId.value
    entity.lockId = this.lockId.value
    entity.startAt = this.schedule.start
    entity.endAt = this.schedule.end
    entity.externalApi = this.apiIdentity?.api
    entity.externalId = this.apiIdentity?.externalId?.value

    return entity
}
