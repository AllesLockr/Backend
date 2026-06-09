package com.alleslocker.backend.application.accessgrant.usecase

import com.alleslocker.backend.application.accessgrant.dto.request.GetAccessGrantsPagedRequestDto
import com.alleslocker.backend.application.accessgrant.dto.response.GetAccessGrantsPagedResponseDto
import com.alleslocker.backend.application.accessgrant.gateway.AccessGrantGateway
import com.alleslocker.backend.application.accessgrant.mapper.toDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.person.PersonId

internal class GetAccessGrantsPagedUseCaseImpl(
    private val accessGrantGateway: AccessGrantGateway,
) : GetAccessGrantsPagedUseCase {
    override fun execute(
        request: GetAccessGrantsPagedRequestDto,
        presenter: OutputBoundary<GetAccessGrantsPagedResponseDto>,
    ) {
        if (request.page < 0) {
            presenter.presentFailure(ErrorResponse.BadRequest("Page must be 0 or greater"))
            return
        }

        if (request.size !in 1..500) {
            presenter.presentFailure(ErrorResponse.BadRequest("Size must be between 1 and 500"))
            return
        }

        val personId =
            request.personId?.let {
                try {
                    PersonId(it)
                } catch (e: IllegalArgumentException) {
                    presenter.presentFailure(ErrorResponse.BadRequest("Invalid personId: ${e.message}"))
                    return
                }
            }

        val lockId =
            request.lockId?.let {
                try {
                    LockId(it)
                } catch (e: IllegalArgumentException) {
                    presenter.presentFailure(ErrorResponse.BadRequest("Invalid lockId: ${e.message}"))
                    return
                }
            }

        val page =
            try {
                accessGrantGateway.getAllGrantsPaged(
                    personId = personId,
                    lockId = lockId,
                    page = request.page,
                    size = request.size,
                )
            } catch (e: Exception) {
                presenter.presentFailure(ErrorResponse.InternalServerError("Failed to load access grants"))
                return
            }

        presenter.present(
            GetAccessGrantsPagedResponseDto(
                page = page.map { it.toDto() },
            ),
        )
    }
}
