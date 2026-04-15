package eu.luftiger.cae.web.common.presenter.factory

import eu.luftiger.cae.web.common.presenter.JsonRestPresenter
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.stereotype.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Component
internal class PresenterFactory(
    private val httpServletResponse: HttpServletResponse,
    private val jacksonConverter: MappingJackson2HttpMessageConverter,
) {
    fun <P : JsonRestPresenter<*>> make(presenter: KClass<P>): P {
        val ctor = presenter.primaryConstructor
            ?: throw IllegalArgumentException("No primary constructor found for presenter: $presenter")

        return ctor.call(httpServletResponse, jacksonConverter)
    }
}