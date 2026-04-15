package eu.luftiger.cae.application.common.factory

import eu.luftiger.cae.application.common.InputBoundary
import kotlin.reflect.KClass

interface UseCaseFactory {
    fun <RQ, RS, I : InputBoundary<RQ, RS>> make(inputBoundary: KClass<out I>): I
}