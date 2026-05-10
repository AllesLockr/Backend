package com.alleslocker.backend.application.person.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.person.dto.request.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.DeletePersonAdapterRequest

interface PersonAdapter : Adapter {
    fun addPerson(request: AddPersonAdapterRequest)
    fun deletePerson(request: DeletePersonAdapterRequest)
}