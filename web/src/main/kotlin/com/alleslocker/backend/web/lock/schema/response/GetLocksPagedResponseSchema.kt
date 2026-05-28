package com.alleslocker.backend.web.lock.schema.response

import com.alleslocker.backend.web.common.model.PageSchema
import com.alleslocker.backend.web.lock.schema.LockSchema

data class GetLocksPagedResponseSchema(
    val page: PageSchema<LockSchema>
)