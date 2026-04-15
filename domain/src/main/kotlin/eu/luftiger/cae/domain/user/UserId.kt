package eu.luftiger.cae.domain.user

import java.util.UUID

@JvmInline
value class UserId(val value: String) {
    init {
        require(value.isNotEmpty()) { "Id cannot be empty" }
    }

    companion object {
        fun generate() = UserId(UUID.randomUUID().toString())
    }
}