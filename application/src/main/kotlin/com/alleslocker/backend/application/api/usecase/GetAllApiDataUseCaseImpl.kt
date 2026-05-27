package com.alleslocker.backend.application.api.usecase

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.application.api.mapper.toDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary

class GetAllApiDataUseCaseImpl(
    private val apiGateway: ApiDataGateway
) : GetAllApiDataUseCase {
    override fun execute(
        request: Unit,
        presenter: OutputBoundary<List<GetApiDataResponseDto>>
    ) {
        try {
            val apiDataList = apiGateway.findAll()

            val response = apiDataList.map { it.toDto() }

            presenter.present(response)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError(e.message ?: "Internal Server Error"))
        }
    }
}