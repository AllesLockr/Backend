package com.alleslocker.backend.web.common.mapper

import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.web.common.model.PageSchema

fun <R, T> PageSchema<T>.toDto(contentMapper: (T) -> R): Page<R> =
    Page(
        content = this.content.map { contentMapper(it) },
        page = this.page,
        size = this.size,
        totalPages = this.totalPages,
        totalElements = this.totalElements,
    )

fun <R, T> Page<T>.toSchema(contentMapper: (T) -> R): PageSchema<R> =
    PageSchema(
        content = this.content.map { contentMapper(it) },
        page = this.page,
        size = this.size,
        totalPages = this.totalPages,
        totalElements = this.totalElements,
    )
