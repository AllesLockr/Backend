package com.alleslocker.backend.lockconnector.lock.client

import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.application.lock.dto.response.FetchedLockDto
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.rest.GenericRestClient

class IseoLockClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) {
    private data class IseoTag(
        val id: Long,
    )

    private data class IseoSmartLockItem(
        val name: String,
        val serialNumber: String,
        val tags: List<IseoTag> = emptyList(),
    )

    private data class IseoSmartLocksPage(
        val content: List<IseoSmartLockItem>,
        val last: Boolean,
        val number: Int,
    )

    fun fetchAll(): FetchLocksAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl
        val locks = mutableListOf<FetchedLockDto>()
        var page = 0

        do {
            val response =
                restClient.get<IseoSmartLocksPage>(
                    endpoint = "$baseUrl/api/v2/smartLocks?page=$page&size=100",
                    headers = mapOf("Authorization" to "Bearer $token"),
                ) ?: break

            response.content.forEach { item ->
                locks +=
                    FetchedLockDto(
                        name = item.name,
                        serialNumber = item.serialNumber,
                        tagId = item.tags.firstOrNull()?.id,
                    )
            }

            if (response.last) break
            page++
        } while (true)

        return FetchLocksAdapterResponse(locks = locks)
    }
}
