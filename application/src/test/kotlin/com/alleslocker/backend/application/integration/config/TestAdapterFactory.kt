package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.common.factory.AdapterFactory
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class TestAdapterFactory(
    private val adapters: List<Adapter>,
) : AdapterFactory {
    override fun <T : Adapter> make(adapter: KClass<out T>): T {
        val match = adapters.firstOrNull { adapter.isInstance(it) }
        if (match != null) {
            @Suppress("UNCHECKED_CAST")
            return match as T
        }

        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            adapter.java.classLoader,
            arrayOf(adapter.java),
        ) { _, method, _ ->
            when (method.returnType) {
                Boolean::class.javaPrimitiveType -> false
                Int::class.javaPrimitiveType -> 0
                Long::class.javaPrimitiveType -> 0L
                else -> null
            }
        } as T
    }
}
