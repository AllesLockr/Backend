package com.alleslocker.backend.domain.person

@JvmInline
value class PersonFirstname(val value: String) {
    init {
        require(value.isNotBlank()) { "Firstname cannot be blank" }
        require(value.length <= 50) { "Firstname cannot be longer than 50 characters" }
    }
}
