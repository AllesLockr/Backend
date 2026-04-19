package com.alleslocker.backend.bootstrap.context

import com.alleslocker.backend.application.common.factory.GatewayFactory
import com.alleslocker.backend.application.common.gateway.Gateway
import com.alleslocker.backend.persistence.factory.GatewayFactoryImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class GatewayContext {
    @Bean
    open fun useGatewayFactory(adapter: List<Gateway>): GatewayFactory = GatewayFactoryImpl(adapter)
}