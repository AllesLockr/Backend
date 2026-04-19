package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.common.gateway.Gateway
import kotlin.reflect.KClass

interface GatewayFactory {
    fun <T : com.alleslocker.backend.application.common.gateway.Gateway> make(gateway: KClass<out T>): T
    fun migrate()
    operator fun <T : com.alleslocker.backend.application.common.gateway.Gateway> get(gateway: KClass<out T>) = make(gateway)
}