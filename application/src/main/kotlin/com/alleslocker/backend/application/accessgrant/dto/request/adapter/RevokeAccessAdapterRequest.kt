package com.alleslocker.backend.application.accessgrant.dto.request.adapter

import com.alleslocker.backend.domain.vendor.AvailableVendors

data class RevokeAccessAdapterRequest(
    val vendor: AvailableVendors,
    val externalId: String,
)
