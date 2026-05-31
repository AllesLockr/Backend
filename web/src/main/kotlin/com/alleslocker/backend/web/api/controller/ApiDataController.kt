package com.alleslocker.backend.web.api.controller

import com.alleslocker.backend.application.api.dto.response.GetApiDataResponseDto
import com.alleslocker.backend.application.api.dto.response.GetImplementedApisResponseDto
import com.alleslocker.backend.application.api.usecase.AddApiDataUseCase
import com.alleslocker.backend.application.api.usecase.GetAllApiDataUseCase
import com.alleslocker.backend.application.api.usecase.GetApiDataUseCase
import com.alleslocker.backend.application.api.usecase.GetImplementedApisUseCase
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.web.api.mapper.toDto
import com.alleslocker.backend.web.api.presenter.AddApiDataPresenter
import com.alleslocker.backend.web.api.presenter.GetAllApiDataPresenter
import com.alleslocker.backend.web.api.presenter.GetApiDataPresenter
import com.alleslocker.backend.web.api.presenter.GetImplementedApisPresenter
import com.alleslocker.backend.web.api.schema.AddApiDataRequestSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "API-Data", description = "Configure 3rd party lock provider APIs.")
@RestController
@RequestMapping("/api/v1/api-data")
class ApiDataController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    @Operation(
        summary = "Add new api credentials for an implemented api.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = SuccessResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "422",
                description = "The 'forApi' value was not valid.",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @PostMapping()
    fun addApiData(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: AddApiDataRequestSchema,
    ) {
        val presenter = AddApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(AddApiDataUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Get all implemented 3rd party APIs.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetImplementedApisResponseDto::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/implemented")
    fun implementedApis() {
        val presenter = GetImplementedApisPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetImplementedApisUseCase::class).execute(Unit, presenter)
    }

    @Operation(
        summary = "Get one Api-Data.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetApiDataResponseDto::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "404",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/{id}")
    fun getApiData(
        @PathVariable("id") id: String,
    ) {
        val presenter = GetApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetApiDataUseCase::class).execute(IdRequest(id), presenter)
    }

    @Operation(
        summary = "Get all Api-Datas.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = GetApiDataResponseDto::class)),
                    ),
                ],
            ),
            ApiResponse(
                responseCode = "500",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/all")
    fun getAllApiData() {
        val presenter = GetAllApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetAllApiDataUseCase::class).execute(Unit, presenter)
    }
}
