package com.alleslocker.backend.web.api.presenter

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetAllApiDataPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<List<GetApiDataResponseDto>>(httpServletResponse, jacksonConverter) {
    override fun present(response: List<GetApiDataResponseDto>) {
        response.presentAsJson()
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
