package com.alleslocker.backend.web.lock.presenter

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.lock.dto.response.SyncLocksResponseDto
import com.alleslocker.backend.web.common.presenter.JsonRestPresenter
import com.alleslocker.backend.web.lock.schema.response.SyncLocksResponseSchema
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

internal class SyncLocksPresenter(
    httpServletResponse: HttpServletResponse,
    jacksonConverter: MappingJackson2HttpMessageConverter,
) : JsonRestPresenter<SyncLocksResponseDto>(httpServletResponse, jacksonConverter) {
    override fun present(response: SyncLocksResponseDto) {
        SyncLocksResponseSchema(synced = response.synced, deleted = response.deleted)
            .presentAsJson(HttpStatus.OK)
    }

    override fun presentFailure(error: ErrorResponse) {
        val status = HttpStatus.resolve(error.status) ?: HttpStatus.INTERNAL_SERVER_ERROR
        error.presentAsJson(status)
    }
}
