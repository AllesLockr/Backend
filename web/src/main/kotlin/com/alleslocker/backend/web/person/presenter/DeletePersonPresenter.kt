package com.alleslocker.backend.web.person.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.person.dto.response.DeletePersonResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.person.schema.response.DeletePersonResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class DeletePersonPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<DeletePersonResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: DeletePersonResponseDto) {
        DeletePersonResponseSchema(
            response.id,
        ).presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.valueOf(error.status))
    }
}
