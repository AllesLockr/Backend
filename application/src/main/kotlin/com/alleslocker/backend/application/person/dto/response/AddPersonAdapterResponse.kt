package com.alleslocker.backend.application.person.dto.response

import com.alleslocker.backend.domain.vendor.AvailableVendors

data class AddPersonAdapterResponse(
    val externalIds: Map<AvailableVendors, String>,
)
