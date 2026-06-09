package com.alleslocker.backend.web.vendor.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.SuccessResponse
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class DeleteVendorDataPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<SuccessResponse>(httpServletResponse, jacksonConverter) {
    override fun present(response: SuccessResponse) {
        val status = HttpStatus.resolve(response.status) ?: HttpStatus.OK
        response.presentAsJson(status)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
