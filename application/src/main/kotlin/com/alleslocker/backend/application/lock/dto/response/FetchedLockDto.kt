package com.alleslocker.backend.application.lock.dto.response

import com.alleslocker.backend.domain.vendor.AvailableVendors

data class FetchedLockDto(
    val name: String,
    val serialNumber: String,
    val tagId: Long?,
    val vendor: AvailableVendors,
    val externalId: String,
)
