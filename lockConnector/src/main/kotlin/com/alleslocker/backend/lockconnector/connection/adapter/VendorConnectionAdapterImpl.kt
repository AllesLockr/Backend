package com.alleslocker.backend.lockconnector.connection.adapter

import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.lockconnector.connection.client.IseoVendorConnectionClientImpl
import com.alleslocker.backend.lockconnector.iseo.client.IseoTokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.stereotype.Component

@Component
class VendorConnectionAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: IseoTokenProvider,
    private val configProvider: ConfigProvider,
) : VendorConnectionAdapter {
    private val iseoVendorConnectionClient: IseoVendorConnectionClientImpl =
        IseoVendorConnectionClientImpl(restClient, tokenProvider, configProvider)

    override fun check(vendor: AvailableVendors, state: VendorState?): VendorState {
        return when (vendor) {
            AvailableVendors.ISEO -> iseoVendorConnectionClient.check(vendor, state)
            AvailableVendors.ASSA_AMOQ -> TODO()
        }
    }
}