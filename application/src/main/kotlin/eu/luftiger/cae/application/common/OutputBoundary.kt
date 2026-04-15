package eu.luftiger.cae.application.common

interface OutputBoundary<R> {
    fun present(response: R)
    fun presentFailure(error: ErrorResponse)
}