package com.alleslocker.backend.lockconnector.accessgrant.adapter

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import org.springframework.stereotype.Component

@Component
internal class AccessGrantAdapterImpl(
    clients: List<AccessGrantClient>,
) : AccessGrantAdapter {
    private val clients: Map<AvailableVendors, AccessGrantClient> =
        clients
            .also { list ->
                val duplicates =
                    list
                        .groupingBy { it.vendor }
                        .eachCount()
                        .filterValues { it > 1 }
                        .keys
                require(duplicates.isEmpty()) {
                    "Duplicate access-grant clients configured for vendors: $duplicates"
                }
            }.associateBy { it.vendor }

    private fun clientFor(vendor: AvailableVendors): AccessGrantClient =
        clients[vendor] ?: throw IllegalArgumentException("No access-grant client for $vendor")

    override fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse =
        clientFor(request.vendor).grant(request)

    override fun revoke(request: RevokeAccessAdapterRequest) = clientFor(request.vendor).revoke(request)
}
