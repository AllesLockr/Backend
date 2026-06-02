package com.alleslocker.backend.web.user.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.user.dto.response.GetUserResponseDto
import com.alleslocker.backend.application.user.dto.response.GetUsersPagedResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.user.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetUserPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetUserResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetUserResponseDto) {
        response.toSchema().presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            is ErrorResponse.NotFound -> error.presentAsJson(HttpStatus.NOT_FOUND)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
