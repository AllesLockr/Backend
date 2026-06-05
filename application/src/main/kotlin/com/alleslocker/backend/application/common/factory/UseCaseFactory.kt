package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.common.InputBoundary
import kotlin.reflect.KClass

interface UseCaseFactory {
    fun <RQ, RS, I : InputBoundary<RQ, RS>> make(inputBoundary: KClass<out I>): I
}
