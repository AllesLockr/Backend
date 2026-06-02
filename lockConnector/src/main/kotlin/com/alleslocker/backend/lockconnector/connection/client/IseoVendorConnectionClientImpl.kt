package com.alleslocker.backend.lockconnector.connection.client

import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.connection.adapter.VendorConnectionClient
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.http.HttpStatus
import java.time.Instant

class IseoVendorConnectionClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : VendorConnectionClient {

    override fun check(vendor: AvailableVendors, state: VendorState?): VendorState {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl

        if (state != null && state.lastChecked > Instant.now().minusSeconds(180))
            return state

        val response = restClient.getBodiless(
            endpoint = "$baseUrl/api/v2/users/me",
            headers = mapOf(
                ("Authorization" to "Bearer $token"),
            ),
        )

        val connectionState = when (response.statusCode) {
            HttpStatus.OK -> VendorConnectionState.CONNECTED
            HttpStatus.FORBIDDEN -> VendorConnectionState.AUTH_FAILED
            else -> VendorConnectionState.DISCONNECTED
        }

        return VendorState(connectionState, Instant.now())
    }
}