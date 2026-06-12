package com.alleslocker.backend.bootstrap.infrastructure.scheduling

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.vendor.usecase.CheckAllVendorConnectionsUseCase
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ScheduledTasks(
    private val useCaseFactory: UseCaseFactory,
    private val logger: Logger,
) {
    @Scheduled(fixedDelayString = "\${app.scheduling.vendor-connection-check}")
    fun checkVendorConnections() {
        logger.info("Starting scheduled vendor connection check")
        useCaseFactory
            .make(CheckAllVendorConnectionsUseCase::class)
            .execute(Unit, LoggingOutputBoundary(logger))
        logger.info("Finished scheduled vendor connection check")
    }
}
