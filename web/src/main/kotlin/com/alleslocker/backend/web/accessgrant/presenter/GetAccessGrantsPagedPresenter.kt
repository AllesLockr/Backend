package com.alleslocker.backend.web.accessgrant.presenter

import com.alleslocker.backend.application.accessgrant.dto.response.GetAccessGrantsPagedResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.accessgrant.mapper.toSchema
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetAccessGrantsPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetAccessGrantsPagedResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetAccessGrantsPagedResponseDto) {
        response.toSchema().presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
