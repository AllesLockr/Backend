package com.alleslocker.backend.application.person.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse

interface PersonAdapter : Adapter {
    fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse
    fun deletePerson(request: DeletePersonAdapterRequest)
}