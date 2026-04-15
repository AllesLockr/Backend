package eu.luftiger.cae.web.common.presenter

import eu.luftiger.cae.application.common.OutputBoundary
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.DelegatingServerHttpResponse
import org.springframework.http.server.ServletServerHttpResponse
import java.io.IOException

internal abstract class JsonRestPresenter<R>(
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter
) : OutputBoundary<R> {

    fun Any.presentAsJson(status: HttpStatus = HttpStatus.OK) {
        writeJson(this, status)
    }

    private fun writeJson(obj: Any, status: HttpStatus) {
        val outputMessage = DelegatingServerHttpResponse(ServletServerHttpResponse(httpServletResponse))
        httpServletResponse.status = status.value()
        try {
            jacksonConverter.write(
                obj,
                MediaType.APPLICATION_JSON,
                outputMessage
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}