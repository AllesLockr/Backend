package com.alleslocker.backend.web.accessgrant.schema.response

import com.alleslocker.backend.web.common.model.PageSchema

data class GetAccessGrantsPagedResponseSchema(
    val page: PageSchema<AccessGrantSchema>,
)
