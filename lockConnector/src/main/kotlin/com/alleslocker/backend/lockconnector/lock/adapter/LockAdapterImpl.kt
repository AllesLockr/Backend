package com.alleslocker.backend.lockconnector.lock.adapter

import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.lock.client.IseoLockClientImpl
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
internal class LockAdapterImpl(
    private val restClient: GenericRestClient,
    @Qualifier("iseoAdminTokenProvider")
    private val iseoTokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : LockAdapter {
    private val iseoClient = IseoLockClientImpl(restClient, iseoTokenProvider, configProvider)

    override fun fetchAllLocks(): FetchLocksAdapterResponse = iseoClient.fetchAll()
}