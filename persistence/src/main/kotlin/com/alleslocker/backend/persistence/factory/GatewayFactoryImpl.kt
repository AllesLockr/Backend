package com.alleslocker.backend.persistence.factory

import com.alleslocker.backend.application.common.factory.GatewayFactory
import com.alleslocker.backend.application.common.gateway.Gateway
import kotlin.reflect.KClass

class GatewayFactoryImpl(
    private val adapter: List<Gateway>
) : GatewayFactory {
    override fun <T : Gateway> make(gateway: KClass<out T>): T {

        val bean = adapter.firstOrNull { gateway.isInstance(it) }
            ?: throw IllegalArgumentException("No adapter found for: $gateway")

        @Suppress("UNCHECKED_CAST")
        return bean as T
    }

    override fun migrate() {}
}