package com.alleslocker.backend.web.auditlog.presenter

import com.alleslocker.backend.application.auditlog.dto.response.GetAuditLogResponseDto
import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.model.Page
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class GetAllAuditLogsPagedPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<Page<GetAuditLogResponseDto>>(httpServletResponse, jacksonConverter) {
    override fun present(response: Page<GetAuditLogResponseDto>) {
        response.presentAsJson()
    }

    override fun presentFailure(error: ErrorResponse) {
        when (error) {
            is ErrorResponse.BadRequest -> error.presentAsJson(HttpStatus.BAD_REQUEST)
            else -> error.presentAsJson(HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }
}
