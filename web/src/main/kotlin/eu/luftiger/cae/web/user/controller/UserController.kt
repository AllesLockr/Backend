package eu.luftiger.cae.web.user.controller

import eu.luftiger.cae.application.common.factory.UseCaseFactory
import eu.luftiger.cae.application.user.usecase.LoginUserUseCase
import eu.luftiger.cae.application.user.usecase.RegisterUserUseCase
import eu.luftiger.cae.web.common.security.JwtService
import eu.luftiger.cae.web.user.mapper.toDto
import eu.luftiger.cae.web.user.presenter.LoginUserPresenter
import eu.luftiger.cae.web.user.presenter.RegisterUserPresenter
import eu.luftiger.cae.web.user.schema.request.LoginUserRequestSchema
import eu.luftiger.cae.web.user.schema.request.RegisterUserRequestSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
    private val jwtService: JwtService
) {
    @PostMapping("/auth/register")
    fun register(@RequestBody request: RegisterUserRequestSchema) {
        val presenter = RegisterUserPresenter(httpServletResponse, jacksonConverter, jwtService)
        useCaseFactory.make(RegisterUserUseCase::class).execute(request.toDto(), presenter)
    }

    @PostMapping("/auth/login")
    fun login(@RequestBody request: LoginUserRequestSchema) {
        val presenter = LoginUserPresenter(httpServletResponse, jacksonConverter, jwtService)
        useCaseFactory.make(LoginUserUseCase::class).execute(request.toDto(), presenter)
    }
}