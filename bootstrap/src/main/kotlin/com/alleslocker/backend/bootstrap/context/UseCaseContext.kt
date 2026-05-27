package com.alleslocker.backend.bootstrap.context

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.factory.AdapterFactory
import com.alleslocker.backend.application.common.factory.GatewayFactory
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.factory.UseCaseFactoryImpl
import com.alleslocker.backend.application.common.security.PasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class UseCaseContext {
    @Bean
    open fun useCaseFactory(
        gatewayFactory: GatewayFactory,
        adapterFactory: AdapterFactory,
        passwordHasher: PasswordHasher,
        logger: Logger
    ): UseCaseFactory = UseCaseFactoryImpl(
        gatewayFactory,
        adapterFactory,
        passwordHasher,
        logger
    )
}