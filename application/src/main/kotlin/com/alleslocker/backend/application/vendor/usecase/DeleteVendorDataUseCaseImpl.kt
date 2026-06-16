package com.alleslocker.backend.application.vendor.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.dto.request.DeleteVendorDataRequestDto
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.auditlog.AuditLog
import com.alleslocker.backend.domain.auditlog.AuditLogId
import com.alleslocker.backend.domain.auditlog.AuditLogMessage
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.vendor.VendorId
import java.time.Instant

class DeleteVendorDataUseCaseImpl(
    private val vendorDataGateway: VendorDataGateway,
    private val logger: Logger,
    private val vendorConnectionAdapter: VendorConnectionAdapter,
) : DeleteVendorDataUseCase {
    override fun execute(
        request: DeleteVendorDataRequestDto,
        presenter: OutputBoundary<SuccessResponse>,
    ) {
        val id = VendorId(request.id)

        val existing =
            vendorDataGateway.findById(id)
                ?: return presenter.presentFailure(ErrorResponse.NotFound("Vendor with id ${request.id} not found"))

        try {
            vendorConnectionAdapter.handleMetadataOnDelete(existing.forVendor, existing.metadata)
        } catch (e: Exception) {
            logger.error("Error while deleting metadata: ", e)
        }

        try {
            vendorDataGateway.deleteById(id)
        } catch (e: Exception) {
            logger.error("Error deleting vendor data", e)
            return presenter.presentFailure(
                ErrorResponse.InternalServerError("Error while deleting requested Vendor-Data"),
            )
        }

        logger.audit(
            AuditLog(
                id = AuditLogId.generate(),
                message = AuditLogMessage("Deleted Vendor-Data for ${existing.forVendor}"),
                performedByUserId = UserId(request.requesterId),
                createdAt = Instant.now(),
            ),
        )

        presenter.present(SuccessResponse.Ok("Deleted Vendor-Data with id ${request.id}"))
    }
}
