package com.alleslocker.backend.domain.api

data class ExternalApiIdentity(
    val api: AvailableApis,
    val externalId: ExternalId,
) {
    override fun equals(other: Any?) = other is ExternalApiIdentity && api == other.api
    override fun hashCode() = api.hashCode()
}