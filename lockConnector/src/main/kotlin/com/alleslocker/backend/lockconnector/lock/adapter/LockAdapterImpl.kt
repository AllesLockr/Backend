package com.alleslocker.backend.lockconnector.lock.adapter

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.lock.client.AssaLockClientImpl
import com.alleslocker.backend.lockconnector.lock.client.IseoLockClientImpl
import org.springframework.stereotype.Component

@Component
internal class LockAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
    private val logger: Logger,
) : LockAdapter {
    private val iseoClient: LockClient =
        IseoLockClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ISEO), configProvider)
    private val assaClient: LockClient =
        AssaLockClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ASSA_AMOQ), configProvider)

    private fun clientFor(vendor: AvailableVendors): LockClient =
        when (vendor) {
            AvailableVendors.ISEO -> iseoClient
            AvailableVendors.ASSA_AMOQ -> assaClient
        }

    override fun fetchAllLocks(): FetchLocksAdapterResponse {
        val targetVendors = configProvider.configuredVendors()

        val locks =
            targetVendors.flatMap { vendor ->
                runCatching { clientFor(vendor).fetchAllLocks().locks }
                    .onFailure {
                        logger.error(
                            "Failed to fetch locks for vendor $vendor",
                            it,
                        )
                    }.getOrElse { emptyList() }
            }
        return FetchLocksAdapterResponse(locks = locks)
    }

    override fun createLock(forVendor: AvailableVendors): Lock {
        when (forVendor) {
            AvailableVendors.ISEO -> return iseoClient.createLock(forVendor)
            AvailableVendors.ASSA_AMOQ -> TODO()
        }
    }

    override fun updateLock(lock: Lock): Lock {
        when (lock.apiIdentity!!.api) {
            AvailableVendors.ISEO -> return iseoClient.updateLock(lock)
            AvailableVendors.ASSA_AMOQ -> TODO()
        }
    }
}
