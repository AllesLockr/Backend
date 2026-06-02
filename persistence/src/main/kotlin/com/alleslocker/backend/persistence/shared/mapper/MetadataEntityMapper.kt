package com.alleslocker.backend.persistence.shared.mapper

import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.persistence.shared.entity.MetadataEntity

fun MetadataEntity.toDomain() = MetadataEntry(key, value)

fun MetadataEntry.toEntity() = MetadataEntity(key, value)