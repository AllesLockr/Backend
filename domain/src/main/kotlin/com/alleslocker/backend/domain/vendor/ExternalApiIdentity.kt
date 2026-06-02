package com.alleslocker.backend.domain.vendor

data class ExternalApiIdentity(
    val api: AvailableVendors,
    val externalId: ExternalId,
)
