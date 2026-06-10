package com.alleslocker.backend.application.accessgrant.mapper

import com.alleslocker.backend.application.accessgrant.dto.response.AccessGrantDto
import com.alleslocker.backend.domain.accessgrant.AccessGrant

fun AccessGrant.toDto() =
    AccessGrantDto(
        grantId = this.id.value,
        personId = this.personId.value,
        lockId = this.lockId.value,
        start = this.schedule.start,
        end = this.schedule.end,
        vendor = this.apiIdentity?.api?.name,
        vendorExternalId = this.apiIdentity?.externalId?.value,
    )
