package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.application.api.usecase.*
import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCase
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCaseImpl
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.usecase.*
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCase
import com.alleslocker.backend.application.user.usecase.RegisterUserUseCaseImpl
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val adapterFactory: AdapterFactory,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) : UseCaseFactory {

    private val useCases: Map<KClass<out InputBoundary<*, *>>, InputBoundary<*, *>> =
        mapOf(
            CreatePersonUseCase::class to CreatePersonUseCaseImpl(
                personGateway = gatewayFactory[PersonGateway::class],
                personAdapter = adapterFactory[PersonAdapter::class]
            ),
            DeletePersonUseCase::class to DeletePersonUseCaseImpl(
                personGateway = gatewayFactory[PersonGateway::class],
                personAdapter = adapterFactory[PersonAdapter::class]
            ),
            GetPersonsPagedUseCase::class to GetPersonsPagedUseCaseImpl(
                personGateway = gatewayFactory[PersonGateway::class],
            ),
            RegisterUserUseCase::class to RegisterUserUseCaseImpl(
                passwordHasher = passwordHasher,
                userGateway = gatewayFactory[UserGateway::class]
            ),
            LoginUserUseCase::class to LoginUserUseCaseImpl(
                passwordHasher = passwordHasher,
                userGateway = gatewayFactory[UserGateway::class]
            ),
            AddApiDataUseCase::class to AddApiDataUseCaseImpl(
                apiDataGateway = gatewayFactory[ApiDataGateway::class], logger = logger,
            ),
            GetImplementedApisUseCase::class to GetImplementedApisUseCaseImpl(),
            GetApiDataUseCase::class to GetApiDataUseCaseImpl(gatewayFactory[ApiDataGateway::class]),
            GetAllApiDataUseCase::class to GetAllApiDataUseCaseImpl(gatewayFactory[ApiDataGateway::class]),
            GetAllAuditLogsPagedUseCase::class to GetAllAuditLogsPagedUseCaseImpl(
                gatewayFactory[AuditLogGateway::class],
                logger
            )
        )

    override fun <RQ, RS, I : InputBoundary<RQ, RS>> make(inputBoundary: KClass<out I>): I {
        @Suppress("UNCHECKED_CAST")
        return useCases[inputBoundary] as I
    }
}