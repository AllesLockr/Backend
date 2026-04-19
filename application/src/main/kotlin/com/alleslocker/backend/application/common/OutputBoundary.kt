package com.alleslocker.backend.application.common

interface OutputBoundary<R> {
    fun present(response: R)
    fun presentFailure(error: com.alleslocker.backend.application.common.ErrorResponse)
}