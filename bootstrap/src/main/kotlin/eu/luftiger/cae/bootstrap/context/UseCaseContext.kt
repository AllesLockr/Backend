package eu.luftiger.cae.bootstrap.context

import eu.luftiger.cae.application.common.factory.GatewayFactory
import eu.luftiger.cae.application.common.factory.UseCaseFactory
import eu.luftiger.cae.application.common.factory.UseCaseFactoryImpl
import eu.luftiger.cae.application.security.PasswordHasher
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
internal open class UseCaseContext {
    @Bean
    open fun useCaseFactory(gatewayFactory: GatewayFactory, passwordHasher: PasswordHasher): UseCaseFactory = UseCaseFactoryImpl(gatewayFactory, passwordHasher)
}