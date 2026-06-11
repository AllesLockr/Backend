package com.alleslocker.backend.lockconnector.auth.common

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.client.IseoOAuthTokenClient
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import org.springframework.stereotype.Component

@Component
class TokenProviderFactory(
    private val configProvider: ConfigProvider,
) {
    private val tokenProviders: Map<AvailableVendors, TokenProvider> =
        mapOf(
            AvailableVendors.ISEO to TokenProvider(IseoOAuthTokenClient(configProvider, AvailableVendors.ISEO)),
        )

    fun make(availableVendor: AvailableVendors): TokenProvider {
        @Suppress("UNCHECKED_CAST")
        return tokenProviders[availableVendor] as TokenProvider
    }
}
