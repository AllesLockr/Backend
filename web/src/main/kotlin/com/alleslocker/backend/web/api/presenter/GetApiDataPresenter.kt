package com.alleslocker.backend.web.api.presenter

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetApiDataPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetApiDataResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetApiDataResponseDto) {
        response.presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.NotFound -> error.presentAsJson(HttpStatus.NOT_FOUND)
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}