package com.alleslocker.backend.web.accessgrant.presenter

import com.alleslocker.backend.application.accessgrant.dto.response.RevokeAccessResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.accessgrant.schema.response.RevokeAccessResponseSchema
import com.alleslocker.backend.web.accessgrant.schema.response.VendorSchema
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class RevokeAccessPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<RevokeAccessResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: RevokeAccessResponseDto) {
        RevokeAccessResponseSchema(
            grantId = response.grantId,
            vendor = VendorSchema.valueOf(response.vendor),
        ).presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
