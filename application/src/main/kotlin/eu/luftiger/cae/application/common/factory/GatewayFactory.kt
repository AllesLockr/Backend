package eu.luftiger.cae.application.common.factory

import eu.luftiger.cae.application.common.gateway.Gateway
import kotlin.reflect.KClass

interface GatewayFactory {
    fun <T : Gateway> make(gateway: KClass<out T>): T
    fun migrate()
    operator fun <T : Gateway> get(gateway: KClass<out T>) = make(gateway)
}