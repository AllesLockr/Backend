package com.alleslocker.backend.application.accessgrant.dto.request.adapter

import com.alleslocker.backend.domain.accessgrant.AccessOperation
import com.alleslocker.backend.domain.vendor.AvailableVendors
import java.time.Instant

data class GrantAccessAdapterRequest(
    val vendor: AvailableVendors,
    val grantId: String,
    val personExternalId: String,
    val lockExternalId: String,
    val lockTagId: String?,
    val operation: AccessOperation,
    val start: Instant,
    val end: Instant,
)