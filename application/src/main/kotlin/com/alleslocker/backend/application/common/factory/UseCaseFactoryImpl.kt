package com.alleslocker.backend.application.common.factory

import com.alleslocker.backend.application.accessgrant.adapter.AccessGrantAdapter
import com.alleslocker.backend.application.accessgrant.gateway.AccessGrantGateway
import com.alleslocker.backend.application.accessgrant.usecase.GetAccessGrantsPagedUseCase
import com.alleslocker.backend.application.accessgrant.usecase.GetAccessGrantsPagedUseCaseImpl
import com.alleslocker.backend.application.accessgrant.usecase.GrantAccessUseCase
import com.alleslocker.backend.application.accessgrant.usecase.GrantAccessUseCaseImpl
import com.alleslocker.backend.application.accessgrant.usecase.RevokeAccessUseCase
import com.alleslocker.backend.application.accessgrant.usecase.RevokeAccessUseCaseImpl
import com.alleslocker.backend.application.auditlog.gateway.AuditLogGateway
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCase
import com.alleslocker.backend.application.auditlog.usecase.GetAllAuditLogsPagedUseCaseImpl
import com.alleslocker.backend.application.common.InputBoundary
import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.common.service.PasswordGeneratorService
import com.alleslocker.backend.application.lock.adapter.LockAdapter
import com.alleslocker.backend.application.lock.gateway.LockGateway
import com.alleslocker.backend.application.lock.usecase.CountLocksUseCase
import com.alleslocker.backend.application.lock.usecase.CountLocksUseCaseImpl
import com.alleslocker.backend.application.lock.usecase.CreateLockUseCase
import com.alleslocker.backend.application.lock.usecase.CreateLockUseCaseImpl
import com.alleslocker.backend.application.lock.usecase.GetLocksPagedUseCase
import com.alleslocker.backend.application.lock.usecase.GetLocksPagedUseCaseImpl
import com.alleslocker.backend.application.lock.usecase.SyncLocksUseCase
import com.alleslocker.backend.application.lock.usecase.SyncLocksUseCaseImpl
import com.alleslocker.backend.application.lock.usecase.UpdateLockUseCase
import com.alleslocker.backend.application.lock.usecase.UpdateLockUseCaseImpl
import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.gateway.PersonGateway
import com.alleslocker.backend.application.person.usecase.CountPersonsUseCase
import com.alleslocker.backend.application.person.usecase.CountPersonsUseCaseImpl
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCase
import com.alleslocker.backend.application.person.usecase.CreatePersonUseCaseImpl
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCase
import com.alleslocker.backend.application.person.usecase.DeletePersonUseCaseImpl
import com.alleslocker.backend.application.person.usecase.GetPersonUseCase
import com.alleslocker.backend.application.person.usecase.GetPersonUseCaseImpl
import com.alleslocker.backend.application.person.usecase.GetPersonsPagedUseCase
import com.alleslocker.backend.application.person.usecase.GetPersonsPagedUseCaseImpl
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.ActivateUserUseCase
import com.alleslocker.backend.application.user.usecase.ActivateUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.AdminResetPasswordUserUseCase
import com.alleslocker.backend.application.user.usecase.AdminResetPasswordUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.ChangeUserRoleUseCase
import com.alleslocker.backend.application.user.usecase.ChangeUserRoleUseCaseImpl
import com.alleslocker.backend.application.user.usecase.CreateUserUseCase
import com.alleslocker.backend.application.user.usecase.CreateUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.DeactivateUserUseCase
import com.alleslocker.backend.application.user.usecase.DeactivateUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.EditUserUseCase
import com.alleslocker.backend.application.user.usecase.EditUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.GetUserUseCase
import com.alleslocker.backend.application.user.usecase.GetUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCaseImpl
import com.alleslocker.backend.application.user.usecase.LoginUserUseCase
import com.alleslocker.backend.application.user.usecase.LoginUserUseCaseImpl
import com.alleslocker.backend.application.user.usecase.RequestUserPasswordChangeUseCase
import com.alleslocker.backend.application.user.usecase.RequestUserPasswordChangeUseCaseImpl
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCase
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCaseImpl
import com.alleslocker.backend.application.vendor.adapter.VendorConnectionAdapter
import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitionsAdapter
import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.application.vendor.usecase.AddVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.AddVendorDataUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.CheckAllVendorConnectionsUseCase
import com.alleslocker.backend.application.vendor.usecase.CheckAllVendorConnectionsUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.DeleteVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.DeleteVendorDataUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.GetAllVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetAllVendorDataUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.GetImplementedVendorsUseCase
import com.alleslocker.backend.application.vendor.usecase.GetImplementedVendorsUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.GetVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.GetVendorDataUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.GetVendorSpecificDefinitionsUseCase
import com.alleslocker.backend.application.vendor.usecase.GetVendorSpecificDefinitionsUseCaseImpl
import com.alleslocker.backend.application.vendor.usecase.UpdateVendorDataUseCase
import com.alleslocker.backend.application.vendor.usecase.UpdateVendorDataUseCaseImpl
import kotlin.reflect.KClass

class UseCaseFactoryImpl(
    private val gatewayFactory: GatewayFactory,
    private val adapterFactory: AdapterFactory,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) : UseCaseFactory {
    private val passwordGeneratorService = PasswordGeneratorService()

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
            GetPersonUseCase::class to
                GetPersonUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                    logger = logger,
                ),
            CountPersonsUseCase::class to
                CountPersonsUseCaseImpl(
                    personGateway = gatewayFactory[PersonGateway::class],
                ),
            GetLocksPagedUseCase::class to
                GetLocksPagedUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                ),
            CountLocksUseCase::class to
                CountLocksUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                    logger = logger,
                ),
            SyncLocksUseCase::class to
                SyncLocksUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                    lockAdapter = adapterFactory[LockAdapter::class],
                    logger = logger,
                ),
            CreateLockUseCase::class to
                CreateLockUseCaseImpl(
                    lockAdapter = adapterFactory[LockAdapter::class],
                    lockGateway = gatewayFactory[LockGateway::class],
                    logger = logger,
                ),
            UpdateLockUseCase::class to
                UpdateLockUseCaseImpl(
                    lockGateway = gatewayFactory[LockGateway::class],
                    lockAdapter = adapterFactory[LockAdapter::class],
                    logger = logger,
                ),
            GrantAccessUseCase::class to
                GrantAccessUseCaseImpl(
                    accessGrantGateway = gatewayFactory[AccessGrantGateway::class],
                    personGateway = gatewayFactory[PersonGateway::class],
                    lockGateway = gatewayFactory[LockGateway::class],
                    accessGrantAdapter = adapterFactory[AccessGrantAdapter::class],
                    logger = logger,
                ),
            RevokeAccessUseCase::class to
                RevokeAccessUseCaseImpl(
                    accessGrantGateway = gatewayFactory[AccessGrantGateway::class],
                    accessGrantAdapter = adapterFactory[AccessGrantAdapter::class],
                    logger = logger,
                ),
            GetAccessGrantsPagedUseCase::class to
                GetAccessGrantsPagedUseCaseImpl(
                    accessGrantGateway = gatewayFactory[AccessGrantGateway::class],
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
            AdminResetPasswordUserUseCase::class to
                AdminResetPasswordUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    passwordHasher = passwordHasher,
                    passwordGeneratorService = passwordGeneratorService,
                    logger = logger,
                ),
            RequestUserPasswordChangeUseCase::class to
                RequestUserPasswordChangeUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            ActivateUserUseCase::class to
                ActivateUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            DeactivateUserUseCase::class to
                DeactivateUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            EditUserUseCase::class to
                EditUserUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            ChangeUserRoleUseCase::class to
                ChangeUserRoleUseCaseImpl(
                    userGateway = gatewayFactory[UserGateway::class],
                    logger = logger,
                ),
            AddVendorDataUseCase::class to
                AddVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                    vendorSpecificDefinitionsAdapter = adapterFactory[VendorSpecificDefinitionsAdapter::class],
                ),
            UpdateVendorDataUseCase::class to
                UpdateVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                    vendorSpecificDefinitionsAdapter = adapterFactory[VendorSpecificDefinitionsAdapter::class],
                ),
            DeleteVendorDataUseCase::class to
                DeleteVendorDataUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    logger = logger,
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                ),
            GetImplementedVendorsUseCase::class to GetImplementedVendorsUseCaseImpl(),
            GetVendorSpecificDefinitionsUseCase::class to
                GetVendorSpecificDefinitionsUseCaseImpl(
                    adapterFactory[VendorSpecificDefinitionsAdapter::class],
                ),
            GetVendorDataUseCase::class to GetVendorDataUseCaseImpl(gatewayFactory[VendorDataGateway::class]),
            GetAllVendorDataUseCase::class to
                GetAllVendorDataUseCaseImpl(
                    gatewayFactory[VendorDataGateway::class],
                    logger,
                ),
            CheckAllVendorConnectionsUseCase::class to
                CheckAllVendorConnectionsUseCaseImpl(
                    vendorDataGateway = gatewayFactory[VendorDataGateway::class],
                    vendorConnectionAdapter = adapterFactory[VendorConnectionAdapter::class],
                    logger = logger,
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
