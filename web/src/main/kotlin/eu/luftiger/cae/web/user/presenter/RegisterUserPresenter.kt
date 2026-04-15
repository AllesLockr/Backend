package eu.luftiger.cae.web.user.presenter

import eu.luftiger.cae.application.common.ErrorResponse
import eu.luftiger.cae.application.user.dto.response.RegisterUserResponseDto
import eu.luftiger.cae.web.common.presenter.JsonRestPresenter
import eu.luftiger.cae.web.common.security.JwtService
import eu.luftiger.cae.web.user.schema.response.RegisterUserResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.apache.coyote.ErrorState
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class RegisterUserPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService
) : JsonRestPresenter<RegisterUserResponseDto>(httpServletResponse, jacksonConverter) {

    override fun present(response: RegisterUserResponseDto) {
        val token = jwtService.generateToken(response.userId)
        RegisterUserResponseSchema(
            userId = response.userId,
            jwtToken = token,
        ).presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.AlreadyExists -> error.presentAsJson(HttpStatus.CONFLICT)
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}