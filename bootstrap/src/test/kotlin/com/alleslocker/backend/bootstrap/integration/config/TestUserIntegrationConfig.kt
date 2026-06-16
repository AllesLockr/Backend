package com.alleslocker.backend.bootstrap.integration.config

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.factory.AdapterFactory
import com.alleslocker.backend.application.common.factory.GatewayFactory
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.factory.UseCaseFactoryImpl
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitionsAdapter
import com.alleslocker.backend.lockconnector.common.AdapterFactoryImpl
import com.alleslocker.backend.persistence.accessgrant.adapter.AccessGrantGatewayAdapter
import com.alleslocker.backend.persistence.auditlog.adapter.AuditLogGatewayAdapter
import com.alleslocker.backend.persistence.factory.GatewayFactoryImpl
import com.alleslocker.backend.persistence.lock.adapter.LockGatewayAdapter
import com.alleslocker.backend.persistence.person.adapter.PersonGatewayAdapter
import com.alleslocker.backend.persistence.user.adapter.UserGatewayAdapter
import com.alleslocker.backend.persistence.vendor.adapter.VendorDataGatewayAdapter
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Primary
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@TestConfiguration(proxyBeanMethods = false)
@ComponentScan(basePackages = ["com.alleslocker.backend.persistence"])
@EnableJpaRepositories(basePackages = ["com.alleslocker.backend.persistence"])
@EntityScan(basePackages = ["com.alleslocker.backend.persistence"])
open class TestUserIntegrationConfig {
    @Bean
    fun passwordHasher(): PasswordHasher = TestPasswordHasher()

    @Bean
    @Primary
    fun testLogger(): Logger = TestLogger()

    @Bean
    fun lockAdapter(): LockAdapter = mock(LockAdapter::class.java)

    @Bean
    fun personAdapter(): PersonAdapter = mock(PersonAdapter::class.java)

    @Bean
    fun accessGrantAdapter(): AccessGrantAdapter = mock(AccessGrantAdapter::class.java)

    @Bean
    fun vendorConnectionAdapter(): VendorConnectionAdapter = mock(VendorConnectionAdapter::class.java)

    @Bean
    fun vendorSpecificDefinitionsAdapter(): VendorSpecificDefinitionsAdapter =
        mock(VendorSpecificDefinitionsAdapter::class.java)

    @Bean
    fun adapterFactory(
        lockAdapter: LockAdapter,
        personAdapter: PersonAdapter,
        accessGrantAdapter: AccessGrantAdapter,
        vendorConnectionAdapter: VendorConnectionAdapter,
        vendorSpecificDefinitionsAdapter: VendorSpecificDefinitionsAdapter,
    ): AdapterFactory =
        AdapterFactoryImpl(
            listOf(
                lockAdapter,
                personAdapter,
                accessGrantAdapter,
                vendorConnectionAdapter,
                vendorSpecificDefinitionsAdapter,
            ),
        )

    @Bean
    fun gatewayFactory(
        userGatewayAdapter: UserGatewayAdapter,
        personGatewayAdapter: PersonGatewayAdapter,
        lockGatewayAdapter: LockGatewayAdapter,
        accessGrantGatewayAdapter: AccessGrantGatewayAdapter,
        vendorDataGatewayAdapter: VendorDataGatewayAdapter,
        auditLogGatewayAdapter: AuditLogGatewayAdapter,
    ): GatewayFactory =
        GatewayFactoryImpl(
            listOf(
                userGatewayAdapter,
                personGatewayAdapter,
                lockGatewayAdapter,
                accessGrantGatewayAdapter,
                vendorDataGatewayAdapter,
                auditLogGatewayAdapter,
            ),
        )

    @Bean
    fun useCaseFactory(
        gatewayFactory: GatewayFactory,
        adapterFactory: AdapterFactory,
        passwordHasher: PasswordHasher,
        logger: Logger,
    ): UseCaseFactory = UseCaseFactoryImpl(gatewayFactory, adapterFactory, passwordHasher, logger)
}
