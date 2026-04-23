package com.alleslocker.backend.web.person.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCase
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCase
import com.alleslocker.backend.web.person.mapper.toDto
import com.alleslocker.backend.web.person.presenter.CreatePersonPresenter
import com.alleslocker.backend.web.person.presenter.DeletePersonPresenter
import com.alleslocker.backend.web.person.schema.request.CreatePersonRequestSchema
import com.alleslocker.backend.web.person.schema.request.DeletePersonRequestSchema
import com.alleslocker.backend.web.person.schema.response.CreatePersonResponseSchema
import com.alleslocker.backend.web.person.schema.response.DeletePersonResponseSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Person")
@RestController
@RequestMapping("/api/v1/person")
class PersonController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter
) {

    @Operation(
        summary = "Create a new person that can be assigned to locks, roles, etc.", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = CreatePersonResponseSchema::class)
                )]
            ),
            ApiResponse(
                responseCode = "409",
                description = "The email is already taken.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Invalid email, first name or last name.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong...rip",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/create")
    fun login(@RequestBody request: CreatePersonRequestSchema) {
        val presenter = CreatePersonPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(CreatePersonUseCase::class).execute(request.toDto(), presenter)
    }

    @Operation(
        summary = "Delete an existing person by user id.", responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = DeletePersonResponseSchema::class)
                )]
            ),
            ApiResponse(
                responseCode = "404",
                description = "Person not found.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "400",
                description = "Not a valid person id.",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            ),
            ApiResponse(
                responseCode = "500",
                description = "Something went wrong...rip",
                content = [Content(
                    mediaType = "application/json",
                    schema = Schema(implementation = ErrorResponse::class)
                )]
            )
        ]
    )
    @PostMapping("/delete")
    fun delete(@RequestBody request: DeletePersonRequestSchema) {
        val presenter = DeletePersonPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(DeletePersonUseCase::class).execute(request.toDto(), presenter)
    }
}