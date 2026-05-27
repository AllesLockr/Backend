package com.alleslocker.backend.web.person.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.person.dto.response.GetPersonsPagedResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.person.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetPersonsPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter
) : JsonRestPresenter<GetPersonsPagedResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: GetPersonsPagedResponseDto) {
        response.toSchema().presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}