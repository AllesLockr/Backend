package com.alleslocker.backend.lockconnector.connectioncheck.adapter

import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.connectioncheck.client.AssaAmoqVendorConnectionClientImpl
import com.alleslocker.backend.lockconnector.connectioncheck.client.IseoVendorConnectionClientImpl
import org.springframework.stereotype.Component

@Component
class VendorConnectionAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : VendorConnectionAdapter {
    private val iseoVendorConnectionClient: IseoVendorConnectionClientImpl =
        IseoVendorConnectionClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ISEO), configProvider)

    private val assaAmoqVendorConnectionClient: AssaAmoqVendorConnectionClientImpl =
        AssaAmoqVendorConnectionClientImpl(
            restClient,
            tokenProviderFactory.make(AvailableVendors.ASSA_AMOQ),
            configProvider,
        )

    override fun check(vendor: AvailableVendors): VendorState =
        when (vendor) {
            AvailableVendors.ISEO -> iseoVendorConnectionClient.check(vendor)
            AvailableVendors.ASSA_AMOQ -> assaAmoqVendorConnectionClient.check(vendor)
        }

    override fun handleMetadata(
        vendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    ) {
        when (vendor) {
            AvailableVendors.ISEO -> iseoVendorConnectionClient.handleMetadata(vendor, metadata)
            AvailableVendors.ASSA_AMOQ -> assaAmoqVendorConnectionClient.handleMetadata(vendor, metadata)
        }
    }
}
