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
import org.springframework.http.MediaType
import org.springframework.web.client.body
import java.time.Instant

class IseoVendorConnectionClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : VendorConnectionClient {
    override fun check(vendor: AvailableVendors): VendorState {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl

        val response =
            restClient.getBodiless(
                endpoint = "$baseUrl/api/v2/users/me",
                headers =
                    mapOf(
                        ("Authorization" to "Bearer $token"),
                    ),
            )

        val connectionState =
            when (response.statusCode) {
                HttpStatus.OK -> VendorConnectionState.CONNECTED
                HttpStatus.FORBIDDEN -> VendorConnectionState.AUTH_FAILED
                else -> VendorConnectionState.DISCONNECTED
            }

        return VendorState(connectionState, Instant.now())
    }

    /***
     * Vendor-Data metadata field for ISEO contains credentials for an installer-account. The installer-account is needed to add new locks to the platform.
     * ***/
    override fun handleMetadata(
        vendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    ): Set<MetadataEntry> {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl

        val installerEmail = metadata.requireValue("installer-email")
        val installerPassword = metadata.requireValue("installer-password")

        val existingIdEntry = metadata.find { it.key == "installer-iseo-id" }

        val iseoIdEntry =
            if (existingIdEntry == null) {
                createInstallerAccount(baseUrl, token, installerEmail, installerPassword)
            } else {
                updateInstallerAccount(baseUrl, token, existingIdEntry.value, installerEmail, installerPassword)
                existingIdEntry
            }

        return metadata + iseoIdEntry
    }

    override fun handleMetadataOnDelete(
        forVendor: AvailableVendors,
        metadata: Set<MetadataEntry>,
    ) {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(forVendor).baseUrl

        val existingId = metadata.find { it.key == "installer-iseo-id" }!!.value

        restClient.delete(
            "$baseUrl/api/v2/users/$existingId?anonymizeAllEvents=false&deleteAllStandardDevices=false&blockAllStandardDevices=false",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    private fun Set<MetadataEntry>.requireValue(key: String): String =
        find { it.key == key }?.value
            ?: throw IllegalStateException(
                "Could not find $key in metadata of ISEO vendor-data! " +
                    "This shouldn't happen because each change on MetadataEntry Set should be validated " +
                    "by vendorDefinitionsAdapter.validateMetadata()",
            )

    private fun createInstallerAccount(
        baseUrl: String,
        token: String,
        email: String,
        password: String,
    ): MetadataEntry {
        val createRequest =
            mapOf(
                "username" to "alles-locker-installer",
                "firstname" to "auto generated",
                "lastname" to "by alles-locker",
                "email" to email,
                "password" to password,
                "roleIds" to listOf(2),
            )

        val response =
            restClient
                .postForResponse(
                    "$baseUrl/api/v2/users",
                    headers = mapOf("Authorization" to "Bearer $token"),
                    body = createRequest,
                    contentType = MediaType.APPLICATION_JSON,
                ).body<IseoCreateUserResponse>()
                ?: throw IllegalStateException("ISEO API returned null body on user creation")

        val userId = requireNotNull(response.id) { "ISEO API returned null user ID" }
        return MetadataEntry("installer-iseo-id", userId.toString())
    }

    private fun updateInstallerAccount(
        baseUrl: String,
        token: String,
        userId: String,
        email: String,
        password: String,
    ) {
        val updateRequest =
            mapOf(
                "email" to email,
                "password" to password,
            )

        restClient
            .putForResponse(
                "$baseUrl/api/v2/users/$userId",
                headers = mapOf("Authorization" to "Bearer $token"),
                body = updateRequest,
                contentType = MediaType.APPLICATION_JSON,
            ).body<IseoCreateUserResponse>()
    }

    private data class IseoCreateUserResponse(
        val id: Int,
    )
}
