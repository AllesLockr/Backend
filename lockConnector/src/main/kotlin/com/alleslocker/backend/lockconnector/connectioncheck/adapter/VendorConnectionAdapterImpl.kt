package com.alleslocker.backend.lockconnector.connectioncheck.adapter

import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.connectioncheck.client.IseoVendorConnectionClientImpl
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class VendorConnectionAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : VendorConnectionAdapter {
    private val iseoVendorConnectionClient: IseoVendorConnectionClientImpl =
        IseoVendorConnectionClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ISEO), configProvider)

    override fun check(vendor: AvailableVendors): VendorState =
        when (vendor) {
            AvailableVendors.ISEO -> iseoVendorConnectionClient.check(vendor)
            else -> VendorState(VendorConnectionState.DISCONNECTED, Instant.now())
        }
}
