package com.alleslocker.backend.web.vendor.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.vendorSpecificField.schema.dto.response.GetVendorSpecificFieldsSchemaResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetVendorDataVendorSpecificFieldsSchemaPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetVendorSpecificFieldsSchemaResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetVendorSpecificFieldsSchemaResponseDto) {
        response.presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
