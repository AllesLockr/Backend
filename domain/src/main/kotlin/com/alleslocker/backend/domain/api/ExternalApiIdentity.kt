package com.alleslocker.backend.domain.api

data class ExternalApiIdentity(
    val api: AvailableApis,
    val externalId: ExternalId,
)