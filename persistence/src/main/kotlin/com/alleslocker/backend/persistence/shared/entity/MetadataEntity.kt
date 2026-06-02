package com.alleslocker.backend.persistence.shared.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class MetadataEntity(
    @Column(name = "meta_key")
    var key: String = "",
    var value: String = ""
)