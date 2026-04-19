package com.alleslocker.backend.web.user.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto
import com.alleslocker.backend.application.user.dto.response.RegisterUserResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.user.schema.response.LoginUserResponseSchema
import com.alleslocker.backend.web.user.schema.response.RegisterUserResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.ErrorState
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class LoginUserPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService
) : JsonRestPresenter<LoginUserResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: LoginUserResponseDto) {
        val token = jwtService.generateToken(response.userId)
        LoginUserResponseSchema(
            userId = response.userId,
            jwtToken = token,
        ).presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.NotFound -> error.presentAsJson(HttpStatus.NOT_FOUND)
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}