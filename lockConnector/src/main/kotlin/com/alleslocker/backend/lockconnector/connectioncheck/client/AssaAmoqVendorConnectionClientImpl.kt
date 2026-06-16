package com.alleslocker.backend.lockconnector.connectioncheck.client

import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.lockconnector.auth.common.TokenProvider
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.connectioncheck.adapter.VendorConnectionClient
import org.springframework.http.HttpStatus
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException
import java.time.Instant

class AssaAmoqVendorConnectionClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : VendorConnectionClient {
    override fun check(vendor: AvailableVendors): VendorState {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl

        val connectionState =
            try {
                val response =
                    restClient.getBodiless(
                        endpoint = "$baseUrl/user",
                        headers = mapOf("Authorization" to "Bearer $token"),
                    )
                when (response.statusCode) {
                    HttpStatus.OK -> VendorConnectionState.CONNECTED
                    else -> VendorConnectionState.DISCONNECTED
                }
            } catch (e: HttpStatusCodeException) {
                when (e.statusCode) {
                    HttpStatus.UNAUTHORIZED, HttpStatus.FORBIDDEN -> VendorConnectionState.AUTH_FAILED
                    else -> VendorConnectionState.DISCONNECTED
                }
            } catch (e: ResourceAccessException) {
                VendorConnectionState.DISCONNECTED
            }

        return VendorState(connectionState, Instant.now())
    }

    override fun handleMetadata(
        vendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    ): Set<MetadataEntry> {
        // No metadata needed for AssaAmock
        return metadata
    }
}
