package com.alleslocker.backend.bootstrap.infrastructure.scheduling

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary

class LoggingOutputBoundary<R>(
    private val logger: Logger,
) : OutputBoundary<R> {
    override fun present(response: R) {
        // No-op for background jobs
    }

    override fun presentFailure(error: ErrorResponse) {
        logger.error("Background job failed: ${error.message}")
    }
}
