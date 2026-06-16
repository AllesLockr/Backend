package com.alleslocker.backend.lockconnector.person.client

import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.common.TokenProvider
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.AssaIdResponse
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.person.adapter.PersonClient
import org.springframework.http.MediaType
import org.springframework.web.client.body

class AssaPersonClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : PersonClient {
    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ASSA_AMOQ).baseUrl
        val response =
            restClient
                .postForResponse(
                    endpoint = "$baseUrl/user",
                    body = mapOf("role" to "USER"), // Types would be USER or ADMIN
                    headers = mapOf("Authorization" to "Bearer $token"),
                    contentType = MediaType.APPLICATION_JSON,
                ).body<AssaIdResponse>()
                ?: throw IllegalStateException("Assa returned empty response body")
        return AddPersonAdapterResponse(externalIds = mapOf(AvailableVendors.ASSA_AMOQ to response.id))
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        val assaId =
            request.externalIds[AvailableVendors.ASSA_AMOQ]
                ?: throw IllegalStateException("Cannot delete person from Assa: no Assa external ID present")
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ASSA_AMOQ).baseUrl
        restClient.delete(
            endpoint = "$baseUrl/user/$assaId",
            headers = mapOf("Authorization" to "Bearer $token"),
        )
    }
}
