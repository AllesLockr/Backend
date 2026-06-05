package com.alleslocker.backend.lockconnector.accessgrant.adapter

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.accessgrant.client.IseoAccessGrantClientImpl
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
internal class AccessGrantAdapterImpl(
    restClient: GenericRestClient,
    @Qualifier("iseoAdminTokenProvider")
    iseoTokenProvider: TokenProvider,
    configProvider: ConfigProvider,
) : AccessGrantAdapter {
    private val clients: Map<AvailableVendors, AccessGrantClient> =
        mapOf(
            AvailableVendors.ISEO to IseoAccessGrantClientImpl(restClient, iseoTokenProvider, configProvider),
            //TODO: AvailableVendors.ASSA_AMOQ to AssaAccessGrantClientImpl(restClient),
        )

    private fun clientFor(vendor: AvailableVendors): AccessGrantClient =
        clients[vendor] ?: throw IllegalArgumentException("No access-grant client for $vendor")

    override fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse = clientFor(request.vendor).grant(request)

    override fun revoke(request: RevokeAccessAdapterRequest) = clientFor(request.vendor).revoke(request)
}