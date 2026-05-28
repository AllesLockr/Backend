package com.alleslocker.backend.application.person.dto.response

import com.alleslocker.backend.domain.api.AvailableApis

data class AddPersonAdapterResponse(
    val externalIds: Map<AvailableApis, String>
)
