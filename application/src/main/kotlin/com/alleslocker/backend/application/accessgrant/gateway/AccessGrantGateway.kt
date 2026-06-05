package com.alleslocker.backend.application.accessgrant.gateway

import com.alleslocker.backend.application.common.gateway.ReadWriteGateway
import com.alleslocker.backend.domain.accessgrant.AccessGrant
import com.alleslocker.backend.domain.accessgrant.AccessGrantId
import com.alleslocker.backend.domain.person.PersonId

interface AccessGrantGateway : ReadWriteGateway<AccessGrant, AccessGrantId> {
    fun findByPersonId(personId: PersonId): List<AccessGrant>
}