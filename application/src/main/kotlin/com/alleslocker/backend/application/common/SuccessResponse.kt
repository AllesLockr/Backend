package com.alleslocker.backend.application.common

sealed class SuccessResponse(val message: String) {
    class Created(message: String) : SuccessResponse(message)
}