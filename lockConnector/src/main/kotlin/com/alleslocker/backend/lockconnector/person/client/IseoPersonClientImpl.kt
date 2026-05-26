package com.alleslocker.backend.lockconnector.person.client

import com.alleslocker.backend.application.person.dto.request.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.lockconnector.iseo.client.IseoTokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.IseoConfig
import com.alleslocker.backend.lockconnector.person.adapter.PersonClient
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.http.MediaType

class IseoPersonClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: IseoTokenProvider,
    private val iseoConfig: IseoConfig,
) : PersonClient {

    private data class IseoCreateUserResponse(val id: Int)
    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        val guestRoleId = 5
        val transformedRequest = mapOf(
            "username" to request.lastname + request.firstname,
            "firstname" to request.firstname,
            "lastname" to request.lastname,
            "email" to request.email,
            "roleIds" to listOf(guestRoleId)
        )
        val token = tokenProvider.getValidToken()
        val response = restClient.post2(
            endpoint = "${iseoConfig.baseUrl}/api/v2/users",
            body = transformedRequest,
            headers = mapOf(
                ("Authorization" to "Bearer $token")
            ),
            contentType = MediaType.APPLICATION_JSON
        ).body(IseoCreateUserResponse::class.java)

        return AddPersonAdapterResponse(id = response?.id?.toString())

    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        val token = tokenProvider.getValidToken()

        restClient.client.delete().uri("${iseoConfig.baseUrl}/api/v2/users/{id}", request.id)
            .headers {
                it.setBearerAuth(token)
            }
            .retrieve()
            .toBodilessEntity()

    }
}