package com.alleslocker.backend.web.user.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.user.mapper.toSchema
import com.alleslocker.backend.web.user.schema.response.LoginUserResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class ResetPasswordUserPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService,
) : JsonRestPresenter<ResetPasswordUserResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: ResetPasswordUserResponseDto) {
        val token = jwtService.generateToken(response.userId)
        response.toSchema(token).presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.NotFound -> error.presentAsJson(HttpStatus.NOT_FOUND)
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            is ErrorResponse.Unauthorized -> error.presentAsJson(HttpStatus.UNAUTHORIZED)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
