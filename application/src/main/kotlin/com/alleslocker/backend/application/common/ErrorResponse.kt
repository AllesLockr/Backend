package com.alleslocker.backend.application.common

sealed class ErrorResponse(
    val message: String,
    val status: Int,
) {
    class NotFound(
        message: String,
    ) : ErrorResponse(message, 404)

    class Unauthorized(
        message: String,
    ) : ErrorResponse(message, 401)

    class BadRequest(
        message: String,
    ) : ErrorResponse(message, 400)

    class AlreadyExists(
        message: String,
    ) : ErrorResponse(message, 409)

    class InternalServerError(
        message: String,
    ) : ErrorResponse(message, 500)

    class UnprocessableEntity(
        message: String,
    ) : ErrorResponse(message, 422)
}
