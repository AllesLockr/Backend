package com.alleslocker.backend.lockconnector.lock.client

import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.application.lock.dto.response.FetchedLockDto
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.common.TokenProvider
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.lock.adapter.LockClient

class AssaLockClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : LockClient {
    private data class AssaLockingDeviceItem(
        val id: String,
        val name: String,
    )

    private data class AssaLockingDevicePage(
        val items: List<AssaLockingDeviceItem> = emptyList(),
    )

    override fun fetchAllLocks(): FetchLocksAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ASSA_AMOQ).baseUrl

        val response =
            restClient.get<AssaLockingDevicePage>(
                endpoint = "$baseUrl/locking-device",
                headers = mapOf("Authorization" to "Bearer $token"),
            ) ?: return FetchLocksAdapterResponse(locks = emptyList())
        val locks =
            response.items.map { item ->
                FetchedLockDto(
                    name = item.name,
                    serialNumber = item.id,
                    tagId = null,
                    vendor = AvailableVendors.ASSA_AMOQ,
                    externalId = item.id,
                )
            }
        return FetchLocksAdapterResponse(locks = locks)
    }
}
