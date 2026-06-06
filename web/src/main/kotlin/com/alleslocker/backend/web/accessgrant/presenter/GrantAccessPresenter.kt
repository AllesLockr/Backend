package com.alleslocker.backend.web.accessgrant.presenter

import com.alleslocker.backend.application.accessgrant.dto.response.GrantAccessResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.accessgrant.schema.response.GrantAccessResponseSchema
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GrantAccessPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GrantAccessResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GrantAccessResponseDto) {
        GrantAccessResponseSchema(
            grantId = response.grantId,
            vendor = response.vendor,
            externalId = response.externalId,
        ).presentAsJson(HttpStatus.CREATED)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
