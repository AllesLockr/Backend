package com.alleslocker.backend.domain.person

import java.util.*

@JvmInline
value class PersonId(val value: String) {
    init {
        require(value.isNotBlank()) { "PersonId cannot be blank" }
    }

    companion object {
        fun generate(): PersonId = PersonId(UUID.randomUUID().toString())
    }
}
