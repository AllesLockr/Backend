package com.alleslocker.backend.lockconnector.person.adapter

import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.lockconnector.iseo.client.IseoTokenProvider
import com.alleslocker.backend.lockconnector.iseo.config.ConfigProvider
import com.alleslocker.backend.lockconnector.person.client.AssaPersonClientImpl
import com.alleslocker.backend.lockconnector.person.client.IMoqPersonClientImpl
import com.alleslocker.backend.lockconnector.person.client.IseoPersonClientImpl
import com.alleslocker.backend.lockconnector.rest.GenericRestClient
import org.springframework.stereotype.Component

@Component
internal class PersonAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: IseoTokenProvider,
    private val configProvider: ConfigProvider
) : PersonAdapter {

    private val iMoqClient: PersonClient = IMoqPersonClientImpl(restClient)
    private val assaClient: PersonClient = AssaPersonClientImpl(restClient)
    private val iseoClient: PersonClient = IseoPersonClientImpl(restClient, tokenProvider, configProvider)

    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {

       /* iMoqClient.addPerson(request)*/
        /*assaClient.addPerson(request)*/
        return iseoClient.addPerson(request)
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        iseoClient.deletePerson(request)
    }

}