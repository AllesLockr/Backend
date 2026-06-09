package com.alleslocker.backend.application.common

sealed class SuccessResponse(
    val message: String,
    val status: Int,
) {
    class Created(
        message: String,
    ) : SuccessResponse(message, status = 201)

    class Ok(
        message: String,
    ) : SuccessResponse(message, status = 200)
}
