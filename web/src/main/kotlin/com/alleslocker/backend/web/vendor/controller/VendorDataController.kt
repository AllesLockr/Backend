package com.alleslocker.backend.web.vendor.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.vendor.dto.response.GetImplementedVendorsResponseDto
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.application.vendor.usecase.AddVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetAllVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetImplementedVendorsUseCase
import com.alleslocker.backend.application.vendor.usecase.GetVendorDataUseCase
import com.alleslocker.backend.web.vendor.mapper.toDto
import com.alleslocker.backend.web.vendor.presenter.AddApiDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetAllApiDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetApiDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetImplementedApisPresenter
import com.alleslocker.backend.web.vendor.schema.AddApiDataRequestSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "Vendor-Data", description = "Configure 3rd party lock provider APIs.")
@RestController
@RequestMapping("/api/v1/vendor-data")
class VendorDataController(
    private val useCaseFactory: UseCaseFactory,
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    @Operation(
        summary = "Add new credentials for an implemented vendor.",
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
    fun addVendorData(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: AddApiDataRequestSchema,
    ) {
        val presenter = AddApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(AddVendorDataUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Get all implemented lock vendors.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetImplementedVendorsResponseDto::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/implemented")
    fun implementedVendors() {
        val presenter = GetImplementedApisPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetImplementedVendorsUseCase::class).execute(Unit, presenter)
    }

    @Operation(
        summary = "Get one Vendor-Data.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetVendorDataResponseDto::class),
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
    fun getVendorData(
        @PathVariable id: String,
    ) {
        val presenter = GetApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetVendorDataUseCase::class).execute(IdRequest(id), presenter)
    }

    @Operation(
        summary = "Get all Vendor-Datas.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        array = ArraySchema(schema = Schema(implementation = GetVendorDataResponseDto::class)),
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
    fun getAllVendorData() {
        val presenter = GetAllApiDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetAllVendorDataUseCase::class).execute(Unit, presenter)
    }
}
