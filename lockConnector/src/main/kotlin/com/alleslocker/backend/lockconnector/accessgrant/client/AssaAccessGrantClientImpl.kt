package com.alleslocker.backend.lockconnector.accessgrant.client

import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.accessgrant.adapter.AccessGrantClient
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.AssaIdResponse
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
internal class AssaAccessGrantClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : AccessGrantClient {
    override val vendor = AvailableVendors.ASSA_AMOQ
    private val tokenProvider = tokenProviderFactory.make(vendor)

    override fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl

        val response =
            restClient
                .postForResponse(
                    endpoint = "$baseUrl/permission",
                    body =
                        mapOf(
                            "userId" to request.personExternalId,
                            "lockingDeviceId" to request.lockExternalId,
                            "operationType" to "OPEN",
                            "permissionType" to "SINGLE_INTERVAL",
                            "start" to request.start.toString(),
                            "end" to request.end.toString(),
                        ),
                    headers = mapOf("Authorization" to "Bearer $token"),
                    contentType = MediaType.APPLICATION_JSON,
                ).body(AssaIdResponse::class.java)
                ?: throw IllegalStateException("ASSA_AMOQ returned empty response body for permission")

        return GrantAccessAdapterResponse(externalId = response.id)
    }

    override fun revoke(request: RevokeAccessAdapterRequest) {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl
        restClient.delete(
            endpoint = "$baseUrl/permission/${request.externalId}",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
