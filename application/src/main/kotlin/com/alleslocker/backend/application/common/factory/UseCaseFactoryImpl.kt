package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.application.api.usecase.AddApiDataUseCase
import com.alleslocker.backend.application.api.usecase.AddApiDataUseCaseImpl
import com.alleslocker.backend.application.api.usecase.GetAllApiDataUseCase
import com.alleslocker.backend.application.api.usecase.GetAllApiDataUseCaseImpl
import com.alleslocker.backend.application.api.usecase.GetApiDataUseCase
import com.alleslocker.backend.application.api.usecase.GetApiDataUseCaseImpl
import com.alleslocker.backend.application.api.usecase.GetImplementedApisUseCase
import com.alleslocker.backend.application.api.usecase.GetImplementedApisUseCaseImpl
import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCase
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCaseImpl
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.usecase.GetLocksPagedUseCase
import com.alleslocker.backend.application.lock.usecase.GetLocksPagedUseCaseImpl
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.usecase.CountPersonsUseCase
import com.alleslocker.backend.application.person.usecase.CountPersonsUseCaseImpl
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCase
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCaseImpl
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCase
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCaseImpl
import com.alleslocker.backend.application.person.usecase.GetPersonsPagedUseCase
import com.alleslocker.backend.application.person.usecase.GetPersonsPagedUseCaseImpl
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCaseImpl
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCaseImpl
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val adapterFactory: AdapterFactory,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) : UseCaseFactory {
    private val useCases: Map<KClass<out InputBoundary<*, *>>, InputBoundary<*, *>> =
        mapOf(
            CreatePersonUseCase::class to
                CreatePersonUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                    personAdapter = adapterFactory[PersonAdapter::class],
                    logger = logger,
                ),
            DeletePersonUseCase::class to
                DeletePersonUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                    personAdapter = adapterFactory[PersonAdapter::class],
                    logger = logger,
                ),
            GetPersonsPagedUseCase::class to
                GetPersonsPagedUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                ),
            CountPersonsUseCase::class to
                CountPersonsUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                ),
            GetLocksPagedUseCase::class to
                GetLocksPagedUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                ),
            LoginUserUseCase::class to
                LoginUserUseCaseImpl(
                    passwordHasher = passwordHasher,
                    userGateway = gatewayFactory[UserGateway::class],
                ),
            GetUsersPagedUseCase::class to
                GetUsersPagedUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            AddApiDataUseCase::class to
                AddApiDataUseCaseImpl(
                    apiDataGateway = gatewayFactory[ApiDataGateway::class],
                    logger = logger,
                ),
            GetImplementedApisUseCase::class to GetImplementedApisUseCaseImpl(),
            GetApiDataUseCase::class to GetApiDataUseCaseImpl(gatewayFactory[ApiDataGateway::class]),
            GetAllApiDataUseCase::class to GetAllApiDataUseCaseImpl(gatewayFactory[ApiDataGateway::class]),
            GetAllAuditLogsPagedUseCase::class to
                GetAllAuditLogsPagedUseCaseImpl(
                    gatewayFactory[AuditLogGateway::class],
                    logger,
                ),
        )

    override fun <RQ, RS, I : InputBoundary<RQ, RS>> make(inputBoundary: KClass<out I>): I {
        @Suppress("UNCHECKED_CAST")
        return useCases[inputBoundary] as I
    }
}
