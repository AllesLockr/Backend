package com.alleslocker.backend.web.lock.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.lock.dto.response.CountLocksResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.lock.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class CountLocksPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<CountLocksResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: CountLocksResponseDto) {
        response.toSchema().presentAsJson()
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
