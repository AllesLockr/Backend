package eu.luftiger.cae.web.user.presenter

import eu.luftiger.cae.application.common.ErrorResponse
import eu.luftiger.cae.application.user.dto.response.LoginUserResponseDto
import eu.luftiger.cae.application.user.dto.response.RegisterUserResponseDto
import eu.luftiger.cae.web.common.presenter.JsonRestPresenter
import eu.luftiger.cae.web.common.security.JwtService
import eu.luftiger.cae.web.user.schema.response.LoginUserResponseSchema
import eu.luftiger.cae.web.user.schema.response.RegisterUserResponseSchema
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