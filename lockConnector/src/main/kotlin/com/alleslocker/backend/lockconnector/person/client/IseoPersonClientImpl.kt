package com.alleslocker.backend.lockconnector.person.client

import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.client.TokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.person.adapter.PersonClient
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.http.MediaType

class IseoPersonClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : PersonClient {
    private data class IseoCreateUserResponse(
        val id: Int,
    )

    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        val guestRoleId = 5
        val transformedRequest =
            mapOf(
                "username" to request.lastname + request.firstname,
                "firstname" to request.firstname,
                "lastname" to request.lastname,
                "email" to request.email,
                "roleIds" to listOf(guestRoleId),
            )

        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl

        val response =
            restClient
                .postForResponse(
                    endpoint = "$baseUrl/api/v2/users",
                    body = transformedRequest,
                    headers =
                        mapOf(
                            ("Authorization" to "Bearer $token"),
                        ),
                    contentType = MediaType.APPLICATION_JSON,
                ).body(IseoCreateUserResponse::class.java)
                ?: throw IllegalStateException("ISEO returned empty response body")

        try {
            restClient.post(
                endpoint = "$baseUrl/api/v2/users/${response.id}/enable",
                body = emptyMap<String, Any>(),
                headers = mapOf("Authorization" to "Bearer $token"),
            )
        } catch (e: Exception) {
            runCatching {
                restClient.delete(
                    endpoint = "$baseUrl/api/v2/users/${response.id}",
                    headers = mapOf("Authorization" to "Bearer $token"),
                )
            }.onFailure { e.addSuppressed(it) }

            throw e
        }
        return AddPersonAdapterResponse(externalIds = mapOf(AvailableVendors.ISEO to response.id.toString()))
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        val iseoId =
            request.externalIds[AvailableVendors.ISEO]
                ?: throw IllegalStateException("Cannot delete person from ISEO: no ISEO external ID present")
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl
        restClient.delete(
            endpoint = "$baseUrl/api/v2/users/$iseoId",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
