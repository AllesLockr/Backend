package com.alleslocker.backend.lockconnector.lock.adapter

import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.lock.client.IseoLockClientImpl
import org.springframework.stereotype.Component

@Component
internal class LockAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : LockAdapter {
    private val iseoClient =
        IseoLockClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ISEO), configProvider)

    override fun fetchAllLocks(): FetchLocksAdapterResponse = iseoClient.fetchAllLocks()
}
