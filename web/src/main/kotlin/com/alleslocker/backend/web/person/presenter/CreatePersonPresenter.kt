package com.alleslocker.backend.web.person.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.person.dto.response.CreatePersonResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.person.schema.response.CreatePersonResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class CreatePersonPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter
) : JsonRestPresenter<CreatePersonResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: CreatePersonResponseDto) {
        CreatePersonResponseSchema(
            response.id
        ).presentAsJson(HttpStatus.CREATED)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            is ErrorResponse.AlreadyExists -> error.presentAsJson(HttpStatus.CONFLICT)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

}