package com.alleslocker.backend.lockconnector.person.adapter

import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.DeletePersonAdapterRequest
import com.alleslocker.backend.lockconnector.config.ConnectionConfig
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.stereotype.Component

@Component
internal class PersonAdapterImpl(
    private val config: ConnectionConfig,
    private val restClient: GenericRestClient
) : PersonAdapter {
    override fun addPerson(request: AddPersonAdapterRequest) {
        val cfg = config.person.addPerson

        // TODO: Fill in correct fields accordingly
        val fields = mapOf(
            "id" to request.id,
            "name" to request.firstname,
            "sur-name" to request.lastname,
            "email" to request.email
        )
        val apiParams = fields.mapKeys { (field, _) -> cfg.parameters[field] ?: field }
        restClient.post(cfg.endpoint, apiParams)
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        val cfg = config.person.deletePerson
        val fields = mapOf(
            "id" to request.id
        )
        val apiParams = fields.mapKeys { (field, _) -> cfg.parameters[field] ?: field }
        restClient.post(cfg.endpoint, apiParams)
    }

}