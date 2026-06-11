package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorState
import java.time.Instant

class CheckAllVendorConnectionsUseCaseImpl(
    private val vendorDataGateway: VendorDataGateway,
    private val vendorConnectionAdapter: VendorConnectionAdapter,
    private val logger: Logger,
) : CheckAllVendorConnectionsUseCase,
    InputBoundary<Unit, Unit> {
    override fun execute(
        request: Unit,
        presenter: OutputBoundary<Unit>,
    ) {
        val allVendors = vendorDataGateway.findAll()

        allVendors.forEach { vendorData ->
            val state =
                try {
                    val result = vendorConnectionAdapter.check(vendorData.forVendor)
                    logger.info(
                        "Connection check for ${vendorData.forVendor}: ${result.connectionState}",
                    )
                    result
                } catch (e: Exception) {
                    logger.error(
                        "Connection check failed for ${vendorData.forVendor}, marking DISCONNECTED",
                        e,
                    )
                    VendorState(VendorConnectionState.DISCONNECTED, Instant.now())
                }

            try {
                vendorDataGateway.save(vendorData.copy(vendorState = state))
            } catch (e: Exception) {
                logger.error(
                    "Failed to save connection state for ${vendorData.forVendor}",
                    e,
                )
            }
        }

        presenter.present(Unit)
    }
}
