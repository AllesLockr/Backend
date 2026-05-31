package com.alleslocker.backend.web.common.model

data class PageSchema<T>(
    val content: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
) {
    fun hasNext() = page < totalPages - 1

    fun hasPrevious() = page > 0

    fun isEmpty() = content.isEmpty()

    fun isFirst() = page == 0

    fun isLast() = !hasNext()

    fun <R> map(mapper: (T) -> R): PageSchema<R> =
        PageSchema<R>(
            content = content.map(mapper),
            page = page,
            size = size,
            totalElements = totalElements,
            totalPages = totalPages,
        )
}
