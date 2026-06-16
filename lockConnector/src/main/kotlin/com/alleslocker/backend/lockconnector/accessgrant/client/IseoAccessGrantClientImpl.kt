package com.alleslocker.backend.lockconnector.accessgrant.client

import com.alleslocker.backend.application.accessgrant.dto.request.adapter.GrantAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.request.adapter.RevokeAccessAdapterRequest
import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.accessgrant.adapter.AccessGrantClient
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import org.springframework.http.MediaType
import org.springframework.stereotype.Component

@Component
internal class IseoAccessGrantClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : AccessGrantClient {
    override val vendor = AvailableVendors.ISEO
    private val tokenProvider = tokenProviderFactory.make(vendor)

    private data class IseoUserTag(
        val id: Int? = null,
        val type: String? = null,
    )

    private data class IseoUserResponse(
        val tags: List<IseoUserTag> = emptyList(),
    )

    private data class IseoCredentialRuleResponse(
        val id: Int,
    )

    override fun grant(request: GrantAccessAdapterRequest): GrantAccessAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(vendor).baseUrl
        val authHeader = mapOf("Authorization" to "Bearer $token")

        val user =
            restClient.get<IseoUserResponse>(
                endpoint = "$baseUrl/api/v2/users/${request.personExternalId}",
                headers = authHeader,
            ) ?: throw IllegalStateException("ISEO returned empty response for user ${request.personExternalId}")

        val guestTagId =
            user.tags.firstOrNull { it.type == "user" }?.id
                ?: throw IllegalStateException("ISEO user ${request.personExternalId} has no user-type tag")

        val lockTagId =
            request.metadata.firstOrNull { it.key == "tagId" }?.value
                ?: throw IllegalStateException("Lock ${request.lockExternalId} has no ISEO lock tag")

        val response =
            restClient
                .postForResponse(
                    endpoint = "$baseUrl/api/v2/credentialRules",
                    body = buildCredentialRuleBody(request, guestTagId, lockTagId),
                    headers = authHeader,
                    contentType = MediaType.APPLICATION_JSON,
                ).body(IseoCredentialRuleResponse::class.java)
                ?: throw IllegalStateException("ISEO returned empty response body for credential rule")

        return GrantAccessAdapterResponse(externalId = response.id.toString())
    }

    override fun revoke(request: RevokeAccessAdapterRequest) {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl
        restClient.delete(
            endpoint = "$baseUrl/api/v2/credentialRules/${request.externalId}",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }

    private fun buildCredentialRuleBody(
        request: GrantAccessAdapterRequest,
        guestTagId: Int,
        lockTagId: String,
    ): Map<String, Any> {
        val lockTagIdNumeric =
            lockTagId.toLongOrNull()
                ?: throw IllegalStateException("ISEO lock tag id '$lockTagId' is not numeric")

        return mapOf(
            "name" to "grant-$guestTagId-$lockTagIdNumeric",
            "extId" to request.grantId,
            "guestTagIds" to listOf(guestTagId),
            "lockTagIds" to listOf(lockTagIdNumeric),
            "guestTagMatchingMode" to "AT_LEAST_ONE_TAG",
            "lockTagMatchingMode" to "AT_LEAST_ONE_TAG",
            "daysOfTheWeek" to listOf(1, 2, 3, 4, 5, 6, 7),
            "dateInterval" to
                mapOf(
                    "from" to request.start.toEpochMilli(),
                    "to" to request.end.toEpochMilli(),
                ),
            "timeInterval" to
                mapOf(
                    "from" to 0,
                    "to" to 86340,
                ),
            "type" to "SMART",
        )
    }
}
