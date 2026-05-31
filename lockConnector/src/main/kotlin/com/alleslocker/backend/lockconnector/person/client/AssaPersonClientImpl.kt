package com.alleslocker.backend.lockconnector.person.client

import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.lockconnector.person.adapter.PersonClient
import com.alleslocker.backend.lockconnector.rest.GenericRestClient

class AssaPersonClientImpl(
    private val restClient: GenericRestClient,
) : PersonClient {
    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        val transformedRequest =
            mapOf(
                Pair("role", "USER"),
            )
        // TODO configure URL in a more central place or read from db?
        restClient.post("http://localhost:8067/user", transformedRequest)

        // TODO assa only saves ids. Save the response with the id in our db
        return AddPersonAdapterResponse(externalIds = emptyMap())
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        TODO("Not yet implemented")
    }
}
