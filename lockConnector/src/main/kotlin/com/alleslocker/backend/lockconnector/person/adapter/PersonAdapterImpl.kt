package com.alleslocker.backend.lockconnector.person.adapter

import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.lockconnector.person.client.AssaPersonClientImpl
import com.alleslocker.backend.lockconnector.person.client.IMoqPersonClientImpl
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.stereotype.Component

@Component
internal class PersonAdapterImpl(
    private val restClient: GenericRestClient
) : PersonAdapter {

    private val iMoqClient: PersonClient = IMoqPersonClientImpl(restClient)
    private val assaClient: PersonClient = AssaPersonClientImpl(restClient)

    override fun addPerson(request: AddPersonAdapterRequest) {
        iMoqClient.addPerson(request)
        assaClient.addPerson(request)
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        //TODO
    }

}