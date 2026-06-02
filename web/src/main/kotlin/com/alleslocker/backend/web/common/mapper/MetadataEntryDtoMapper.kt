package com.alleslocker.backend.web.common.mapper

import com.alleslocker.backend.application.common.dto.MetadataEntryDto
import com.alleslocker.backend.web.common.schema.MetadataEntrySchema

fun MetadataEntryDto.toSchema() =
    MetadataEntrySchema(
        key = key,
        value = value,
    )
