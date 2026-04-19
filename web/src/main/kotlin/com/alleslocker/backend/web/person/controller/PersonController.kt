package com.alleslocker.backend.web.person.controller

import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCase
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCase
import com.alleslocker.backend.web.person.mapper.toDto
import com.alleslocker.backend.web.person.presenter.CreatePersonPresenter
import com.alleslocker.backend.web.person.presenter.DeletePersonPresenter
import com.alleslocker.backend.web.person.schema.request.CreatePersonRequestSchema
import com.alleslocker.backend.web.person.schema.request.DeletePersonRequestSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/person")
class PersonController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter
) {

    @PostMapping("/create")
    fun login(@RequestBody request: CreatePersonRequestSchema) {
        val presenter = CreatePersonPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(CreatePersonUseCase::class).execute(request.toDto(), presenter)
    }
    @PostMapping("/delete")
    fun delete(@RequestBody request: DeletePersonRequestSchema) {
        val presenter = DeletePersonPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(DeletePersonUseCase::class).execute(request.toDto(), presenter)
    }
}