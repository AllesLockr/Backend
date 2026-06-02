package com.alleslocker.backend.persistence.shared.mapper

import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.persistence.shared.entity.MetadataEntryEntity

fun MetadataEntryEntity.toDomain() = MetadataEntry(key, value)

fun MetadataEntry.toEntity() = MetadataEntryEntity(key, value)
