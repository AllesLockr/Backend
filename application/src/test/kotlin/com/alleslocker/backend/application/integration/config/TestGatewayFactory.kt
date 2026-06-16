package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.factory.GatewayFactory
import com.alleslocker.backend.application.common.gateway.Gateway
import java.lang.reflect.Proxy
import kotlin.reflect.KClass

class TestGatewayFactory(
    private val gateways: List<Gateway>,
) : GatewayFactory {
    override fun <T : Gateway> make(gateway: KClass<out T>): T {
        val match = gateways.firstOrNull { gateway.isInstance(it) }
        if (match != null) {
            @Suppress("UNCHECKED_CAST")
            return match as T
        }

        @Suppress("UNCHECKED_CAST")
        return Proxy.newProxyInstance(
            gateway.java.classLoader,
            arrayOf(gateway.java),
        ) { _, method, _ ->
            when (method.returnType) {
                Boolean::class.javaPrimitiveType -> false
                Int::class.javaPrimitiveType -> 0
                Long::class.javaPrimitiveType -> 0L
                else -> null
            }
        } as T
    }

    override fun migrate() {}
}
