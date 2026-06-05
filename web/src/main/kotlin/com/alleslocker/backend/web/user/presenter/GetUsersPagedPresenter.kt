package com.alleslocker.backend.web.user.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.user.dto.response.GetUsersPagedResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.user.mapper.toSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetUsersPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetUsersPagedResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetUsersPagedResponseDto) {
        response.toSchema().presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.valueOf(error.status))
    }
}
