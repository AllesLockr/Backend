package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.security.PasswordHasher
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCase
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCaseImpl
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val passwordHasher: PasswordHasher,
) : UseCaseFactory {

    private val useCases: Map<KClass<out InputBoundary<*, *>>, InputBoundary<*, *>> =
        mapOf(
            RegisterUserUseCase::class to RegisterUserUseCaseImpl(
                passwordHasher = passwordHasher,
                userGateway = gatewayFactory[UserGateway::class]
            ),
            LoginUserUseCase::class to LoginUserUseCaseImpl(
                passwordHasher = passwordHasher,
                userGateway = gatewayFactory[UserGateway::class]
            )
        )

    override fun <RQ, RS, I : InputBoundary<RQ, RS>> make(inputBoundary: KClass<out I>): I {
        @Suppress("UNCHECKED_CAST")
        return useCases[inputBoundary] as I
    }
}