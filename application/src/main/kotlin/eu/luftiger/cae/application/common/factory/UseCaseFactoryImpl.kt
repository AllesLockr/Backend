package eu.luftiger.cae.application.common.factory

import eu.luftiger.cae.application.common.InputBoundary
import eu.luftiger.cae.application.security.PasswordHasher
import eu.luftiger.cae.application.task.usecase.GetTaskUseCase
import eu.luftiger.cae.application.task.usecase.GetTaskUseCaseImpl
import eu.luftiger.cae.application.user.gateway.UserGateway
import eu.luftiger.cae.application.user.usecase.LoginUserUseCase
import eu.luftiger.cae.application.user.usecase.LoginUserUseCaseImpl
import eu.luftiger.cae.application.user.usecase.RegisterUserUseCase
import eu.luftiger.cae.application.user.usecase.RegisterUserUseCaseImpl
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val passwordHasher: PasswordHasher,
) : UseCaseFactory {

    private val useCases: Map<KClass<out InputBoundary<*, *>>, InputBoundary<*, *>> =
        mapOf(
            GetTaskUseCase::class to GetTaskUseCaseImpl(),
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