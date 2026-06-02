package com.alleslocker.backend.persistence.shared.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class MetadataEntryEntity(
    @Column(name = "meta_key")
    val key: String = "",
    val value: String = "",
)
