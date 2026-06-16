package com.alleslocker.backend.lockconnector.lock.client

import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.application.lock.dto.response.FetchedLockDto
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
import com.alleslocker.backend.lockconnector.auth.common.TokenProvider
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.lock.adapter.LockClient
import org.springframework.http.MediaType.APPLICATION_JSON

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

    private data class AssaCreatedLockingDevice(
        val id: String,
        val name: String? = null,
        val serialNumber: String? = null,
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

    override fun createLock(forVendor: AvailableVendors): Lock {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(forVendor).baseUrl

        val createdDevice =
            restClient
                .postForResponse(
                    endpoint = "$baseUrl/locking-device",
                    body = mapOf("name" to "$forVendor"),
                    headers = mapOf("Authorization" to "Bearer $token"),
                    contentType = APPLICATION_JSON,
                ).body(AssaCreatedLockingDevice::class.java)
                ?: throw IllegalStateException("$forVendor returned empty response body for locking-device")

        return Lock(
            id = LockId.generate(),
            name = LockName(createdDevice.name ?: "$forVendor"),
            serialNumber = LockSerialNumber(createdDevice.serialNumber ?: createdDevice.id),
            apiIdentity =
                ExternalApiIdentity(
                    api = forVendor,
                    externalId = ExternalId(createdDevice.id),
                ),
        )
    }

    override fun updateLock(lock: Lock): Lock {
        // Not implemented by ASSA
        return lock
    }
}
