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
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            is ErrorResponse.NotFound -> error.presentAsJson(HttpStatus.NOT_FOUND)
            is ErrorResponse.UnprocessableEntity -> error.presentAsJson(HttpStatus.UNPROCESSABLE_ENTITY)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}