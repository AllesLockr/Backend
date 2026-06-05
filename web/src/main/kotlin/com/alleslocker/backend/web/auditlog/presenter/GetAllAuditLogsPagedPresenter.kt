package com.alleslocker.backend.web.auditlog.presenter

import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogsPagedResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetAllAuditLogsPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<GetAuditLogsPagedResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: GetAuditLogsPagedResponseDto) {
        response.presentAsJson()
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
