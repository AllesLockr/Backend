package com.alleslocker.backend.web.api.presenter

import com.alleslocker.backend.application.api.dto.response.GetImplementedApisResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetImplementedApisPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetImplementedApisResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetImplementedApisResponseDto) {
        response.presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
