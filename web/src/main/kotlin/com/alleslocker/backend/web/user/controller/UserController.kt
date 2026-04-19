package com.alleslocker.backend.web.user.controller

import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCase
import com.alleslocker.backend.web.common.security.JwtService
import com.alleslocker.backend.web.user.mapper.toDto
import com.alleslocker.backend.web.user.presenter.LoginUserPresenter
import com.alleslocker.backend.web.user.presenter.RegisterUserPresenter
import com.alleslocker.backend.web.user.schema.request.LoginUserRequestSchema
import com.alleslocker.backend.web.user.schema.request.RegisterUserRequestSchema
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