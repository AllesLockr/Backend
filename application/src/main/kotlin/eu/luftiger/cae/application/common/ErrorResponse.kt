package eu.luftiger.cae.application.common

sealed class ErrorResponse(val message: String) {
    class NotFound(message: String) : ErrorResponse(message)
    class Unauthorized(message: String) : ErrorResponse(message)
    class BadRequest(message: String) : ErrorResponse(message)
    class AlreadyExists(message: String) : ErrorResponse(message)
    class InternalServerError(message: String) : ErrorResponse(message)
}