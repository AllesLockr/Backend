package com.alleslocker.backend.web.person.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.person.dto.response.CountPersonsResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.person.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class CountPersonsPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter
) : JsonRestPresenter<CountPersonsResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: CountPersonsResponseDto) {
        response.toSchema().presentAsJson()
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
    }

}