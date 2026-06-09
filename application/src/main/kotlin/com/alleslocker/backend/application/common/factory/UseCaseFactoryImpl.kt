package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCase
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCaseImpl
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.common.service.PasswordGeneratorService
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.usecase.*
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.usecase.*
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.*
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.application.vendor.usecase.*
import com.alleslocker.backend.application.vendorSpecificField.schema.lock.LockVendorSpecificFieldSchemaRegistry
import com.alleslocker.backend.application.vendorSpecificField.schema.lock.provider.IseoLockSchemaProvider
import com.alleslocker.backend.application.vendorSpecificField.schema.vendor.VendorDataVendorSpecificFieldSchemaRegistry
import com.alleslocker.backend.application.vendorSpecificField.schema.vendor.provider.IseoVendorDataSchemaProvider
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val adapterFactory: AdapterFactory,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) : UseCaseFactory {
    private val passwordGeneratorService = PasswordGeneratorService()

    private val vendorDataVendorSpecificFieldSchemaRegistry =
        VendorDataVendorSpecificFieldSchemaRegistry(
            providers =
                listOf(
                    IseoVendorDataSchemaProvider(),
                ),
        )

    private val lockVendorSpecificFieldSchemaRegistry =
        LockVendorSpecificFieldSchemaRegistry(
            providers =
                listOf(
                    IseoLockSchemaProvider(),
                ),
        )

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
            SyncLocksUseCase::class to
                SyncLocksUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                    lockAdapter = adapterFactory[LockAdapter::class],
                    logger = logger,
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
            GetUserUseCase::class to
                GetUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            ResetPasswordUserUseCase::class to
                ResetPasswordUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    passwordHasher = passwordHasher,
                    logger = logger,
                ),
            CreateUserUseCase::class to
                CreateUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    passwordHasher = passwordHasher,
                    logger = logger,
                    passwordGeneratorService = passwordGeneratorService,
                ),
            AddVendorDataUseCase::class to
                AddVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                ),
            UpdateVendorDataUseCase::class to
                UpdateVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                ),
            DeleteVendorDataUseCase::class to
                DeleteVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                ),
            GetImplementedVendorsUseCase::class to GetImplementedVendorsUseCaseImpl(),
            GetVendorDataVendorSpecificFieldsSchemaUseCase::class to
                GetVendorDataVendorSpecificFieldsSchemaUseCaseImpl(vendorDataVendorSpecificFieldSchemaRegistry),
            GetLockVendorSpecificFieldsSchemaUseCase::class to
                GetLockVendorSpecificFieldsSchemaUseCaseImpl(lockVendorSpecificFieldSchemaRegistry),
            GetVendorDataUseCase::class to GetVendorDataUseCaseImpl(gatewayFactory[VendorDataGateway::class]),
            GetAllVendorDataUseCase::class to
                GetAllVendorDataUseCaseImpl(
                    gatewayFactory[VendorDataGateway::class],
                    logger,
                ),
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
