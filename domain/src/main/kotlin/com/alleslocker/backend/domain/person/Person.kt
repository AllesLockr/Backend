package com.alleslocker.backend.domain.person

import com.alleslocker.backend.domain.role.Role

data class Person(
    val id: PersonId,
    val firstname: PersonFirstname,
    val lastname: PersonLastname,
    val email: PersonEmail,
    val roles: Set<Role>
)