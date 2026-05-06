package com.alleslocker.backend.lockconnector.factory

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.common.factory.AdapterFactory
import kotlin.reflect.KClass

class AdapterFactoryImpl(
    private val adapters: List<Adapter>
) : AdapterFactory {
    override fun <T : Adapter> make(adapter: KClass<out T>): T {
        val bean = adapters.firstOrNull { adapter.isInstance(it) }
            ?: throw IllegalArgumentException("No adapter found for: $adapter")

        @Suppress("UNCHECKED_CAST")
        return bean as T
    }
}