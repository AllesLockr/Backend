package com.alleslocker.backend.web.vendor.controller

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.IdRequest
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.vendor.dto.request.DeleteVendorDataRequestDto
import com.alleslocker.backend.application.vendor.dto.request.GetVendorSpecificDefinitionsRequestDto
import com.alleslocker.backend.application.vendor.dto.response.GetImplementedVendorsResponseDto
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.application.vendor.dto.response.GetVendorSpecificDefinitionsResponseDto
import com.alleslocker.backend.application.vendor.usecase.AddVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.DeleteVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetAllVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetImplementedVendorsUseCase
import com.alleslocker.backend.application.vendor.usecase.GetVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetVendorSpecificDefinitionsUseCase
import com.alleslocker.backend.application.vendor.usecase.UpdateVendorDataUseCase
import com.alleslocker.backend.web.vendor.mapper.toDto
import com.alleslocker.backend.web.vendor.presenter.AddVendorDataPresenter
import com.alleslocker.backend.web.vendor.presenter.DeleteVendorDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetAllVendorDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetImplementedVendorsPresenter
import com.alleslocker.backend.web.vendor.presenter.GetVendorDataPresenter
import com.alleslocker.backend.web.vendor.presenter.GetVendorSpecificDefinitionsPresenter
import com.alleslocker.backend.web.vendor.presenter.UpdateVendorDataPresenter
import com.alleslocker.backend.web.vendor.schema.AddVendorDataRequestSchema
import com.alleslocker.backend.web.vendor.schema.UpdateVendorDataRequestSchema
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
        summary = "Add new credentials for an implemented vendor. Check /definitions/{forVendor} for required metadata fields.",
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
        @RequestBody request: AddVendorDataRequestSchema,
    ) {
        val presenter = AddVendorDataPresenter(httpServletResponse, jacksonConverter)
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
        val presenter = GetImplementedVendorsPresenter(httpServletResponse, jacksonConverter)
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
        val presenter = GetVendorDataPresenter(httpServletResponse, jacksonConverter)
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
        val presenter = GetAllVendorDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(GetAllVendorDataUseCase::class).execute(Unit, presenter)
    }

    @Operation(
        summary = "Update one vendor-data.",
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
                responseCode = "500",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
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
                responseCode = "422",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @PutMapping()
    fun updateVendorData(
        @AuthenticationPrincipal requesterId: String,
        @RequestBody request: UpdateVendorDataRequestSchema,
    ) {
        val presenter = UpdateVendorDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory.make(UpdateVendorDataUseCase::class).execute(request.toDto(requesterId), presenter)
    }

    @Operation(
        summary = "Delete one vendor-data by id.",
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
                responseCode = "500",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
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
        ],
    )
    @DeleteMapping("/{id}")
    fun deleteVendorData(
        @AuthenticationPrincipal requesterId: String,
        @PathVariable id: String,
    ) {
        val presenter = DeleteVendorDataPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory
            .make(DeleteVendorDataUseCase::class)
            .execute(DeleteVendorDataRequestDto(id, requesterId), presenter)
    }

    @Operation(
        summary = "Get vendor specific fields that should be sent when creating a new vendor-data.",
        responses = [
            ApiResponse(
                responseCode = "200",
                description = "Success",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = GetVendorSpecificDefinitionsResponseDto::class),
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
            ApiResponse(
                responseCode = "422",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ErrorResponse::class),
                    ),
                ],
            ),
        ],
    )
    @GetMapping("/definitions/{forVendor}")
    fun getVendorSpecificDefinitions(
        @PathVariable forVendor: String,
    ) {
        val presenter = GetVendorSpecificDefinitionsPresenter(httpServletResponse, jacksonConverter)
        useCaseFactory
            .make(GetVendorSpecificDefinitionsUseCase::class)
            .execute(GetVendorSpecificDefinitionsRequestDto(forVendor), presenter)
    }
}
