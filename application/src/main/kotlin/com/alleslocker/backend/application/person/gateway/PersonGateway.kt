package com.alleslocker.backend.application.person.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.application.person.dto.filter.PersonFilterDto
import com.alleslocker.backend.domain.person.Person
import com.alleslocker.backend.domain.person.PersonId

interface PersonGateway : ReadWriteGateway<Person, PersonId> {
    fun existsByEmail(email: String): Boolean
    fun getAllPersonsPaged(filter: PersonFilterDto, page: Int = 1, size: Int = 10): Page<Person>
}