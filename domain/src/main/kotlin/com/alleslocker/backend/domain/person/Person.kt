package com.alleslocker.backend.domain.person

import com.alleslocker.backend.domain.api.ExternalApiIdentity
import com.alleslocker.backend.domain.role.Role
import com.alleslocker.backend.domain.shared.MetadataEntry

data class Person(
    val id: PersonId,
    val firstname: PersonFirstname,
    val lastname: PersonLastname,
    val email: PersonEmail,
    val roles: Set<Role>,
    val metadata: Set<MetadataEntry> = emptySet(),
    val apiIdentities: Set<ExternalApiIdentity> = emptySet(),
)
