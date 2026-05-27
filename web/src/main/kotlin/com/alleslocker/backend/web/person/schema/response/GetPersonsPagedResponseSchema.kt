package com.alleslocker.backend.web.person.schema.response

import com.alleslocker.backend.web.common.model.PageSchema
import com.alleslocker.backend.web.person.schema.PersonSchema

data class GetPersonsPagedResponseSchema(
    val page: PageSchema<PersonSchema>
)