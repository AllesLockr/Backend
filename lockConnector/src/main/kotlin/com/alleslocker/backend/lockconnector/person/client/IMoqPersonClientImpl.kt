package com.alleslocker.backend.lockconnector.person.client


import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.lockconnector.person.adapter.PersonClient
import com.alleslocker.backend.lockconnector.rest.GenericRestClient

class IMoqPersonClientImpl(private val restClient: GenericRestClient) : PersonClient {
    data class RoleResponse(val id: Int, val name: String)

    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        //TODO configure URL in a more central place or read from db?
        val availableRoles: List<RoleResponse>? = restClient.get("http://localhost:8089/api/v2/roles")

        val transformedRequest = mapOf(
            Pair("username", request.lastname + request.firstname),
            Pair("firstName", request.firstname),
            Pair("lastName", request.lastname),
            Pair("roleIds", listOf(availableRoles?.find { it.name == "person" }?.id))
        )

        restClient.post("http://localhost:8089/api/v2/users", transformedRequest)
        return AddPersonAdapterResponse(id = null)
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        TODO("Not yet implemented")
    }
}