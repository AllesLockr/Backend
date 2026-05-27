package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.application.api.mapper.toDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.domain.api.ApiId

class GetApiDataUseCaseImpl(private val apiDataGateway: ApiDataGateway) : GetApiDataUseCase {
    override fun execute(
        request: IdRequest,
        presenter: OutputBoundary<GetApiDataResponseDto>,
    ) {
        if (request.id.isBlank()) {
            presenter.presentFailure(ErrorResponse.BadRequest("ID cannot be blank"))
            return
        }

        val apiData = apiDataGateway.findById(ApiId(request.id))

        if (apiData == null) {
            presenter.presentFailure(ErrorResponse.NotFound("ID not found"))
            return
        }

        val response = apiData.toDto()

        return presenter.present(response)
    }
}