package com.alleslocker.backend.web.lock.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.lock.dto.response.GetLocksPagedResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.lock.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetLocksPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter
) : JsonRestPresenter<GetLocksPagedResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: GetLocksPagedResponseDto) {
        response.toSchema().presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}