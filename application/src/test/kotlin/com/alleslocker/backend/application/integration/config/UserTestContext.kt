package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.factory.UseCaseFactoryImpl
import com.alleslocker.backend.application.common.gateway.Gateway
import com.alleslocker.backend.application.common.security.PasswordHasher

data class UserTestContext(
    val userGateway: TestUserGatewayAdapter,
    val passwordHasher: PasswordHasher,
    val logger: TestLogger,
    val useCaseFactory: UseCaseFactory,
)

fun createUserTestContext(): UserTestContext {
    val userGateway = TestUserGatewayAdapter()
    val passwordHasher = TestPasswordHasher()
    val logger = TestLogger()
    val gatewayFactory = TestGatewayFactory(listOf<Gateway>(userGateway))
    val adapterFactory = TestAdapterFactory(emptyList<Adapter>())
    val useCaseFactory = UseCaseFactoryImpl(gatewayFactory, adapterFactory, passwordHasher, logger)
    return UserTestContext(userGateway, passwordHasher, logger, useCaseFactory)
}
