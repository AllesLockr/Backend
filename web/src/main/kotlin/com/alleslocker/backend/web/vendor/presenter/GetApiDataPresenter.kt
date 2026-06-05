package com.alleslocker.backend.web.vendor.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.vendor.dto.response.GetVendorDataResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetApiDataPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetVendorDataResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetVendorDataResponseDto) {
        response.presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        error.presentAsJson(HttpStatus.valueOf(error.status))
    }
}
