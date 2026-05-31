package com.alleslocker.backend.application.person.dto.request.adapter

import com.alleslocker.backend.domain.api.AvailableApis

data class DeletePersonAdapterRequest(
    val externalIds: Map<AvailableApis, String>,
)
