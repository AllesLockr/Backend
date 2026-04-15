package eu.luftiger.cae.bootstrap.context

import eu.luftiger.cae.application.common.factory.GatewayFactory
import eu.luftiger.cae.application.common.gateway.Gateway
import eu.luftiger.cae.persistence.factory.GatewayFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class GatewayContext {
    @Bean
    open fun useGatewayFactory(adapter: List<Gateway>): GatewayFactory = GatewayFactoryImpl(adapter)
}