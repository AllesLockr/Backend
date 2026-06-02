package com.alleslocker.backend.domain.shared

data class MetadataEntry(
    val key: String,
    val value: String
) {
    init {
        require(key.isNotBlank()) { "Key must not be blank" }
        require(value.isNotBlank()) { "Value must not be blank" }
    }
}