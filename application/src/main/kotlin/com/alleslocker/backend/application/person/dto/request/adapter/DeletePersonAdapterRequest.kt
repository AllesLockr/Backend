package com.alleslocker.backend.application.person.dto.request.adapter

import com.alleslocker.backend.domain.vendor.AvailableVendors

data class DeletePersonAdapterRequest(
    val externalIds: Map<AvailableVendors, String>,
)
