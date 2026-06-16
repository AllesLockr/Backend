package com.alleslocker.backend.lockconnector.person.adapter

import com.alleslocker.backend.application.person.adapter.PersonAdapter
import com.alleslocker.backend.application.person.dto.request.adapter.AddPersonAdapterRequest
import com.alleslocker.backend.application.person.dto.request.adapter.DeletePersonAdapterRequest
import com.alleslocker.backend.application.person.dto.response.AddPersonAdapterResponse
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.lockconnector.auth.common.TokenProviderFactory
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.person.client.AssaPersonClientImpl
import com.alleslocker.backend.lockconnector.person.client.IseoPersonClientImpl
import org.springframework.stereotype.Component

@Component
internal class PersonAdapterImpl(
    private val restClient: GenericRestClient,
    private val tokenProviderFactory: TokenProviderFactory,
    private val configProvider: ConfigProvider,
) : PersonAdapter {
    private val assaClient: PersonClient =
        AssaPersonClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ASSA_AMOQ), configProvider)
    private val iseoClient: PersonClient =
        IseoPersonClientImpl(restClient, tokenProviderFactory.make(AvailableVendors.ISEO), configProvider)

    private fun clientFor(vendor: AvailableVendors): PersonClient =
        when (vendor) {
            AvailableVendors.ASSA_AMOQ -> assaClient
            AvailableVendors.ISEO -> iseoClient
        }

    override fun addPerson(request: AddPersonAdapterRequest): AddPersonAdapterResponse {
        val targetVendors = configProvider.configuredVendors()
        val merged = mutableMapOf<AvailableVendors, String>()
        try {
            targetVendors.forEach { vendor ->
                clientFor(vendor).addPerson(request).externalIds.forEach { (key, value) -> merged[key] = value }
            }
        } catch (e: Exception) {
            merged.forEach { (vendor, externalId) ->
                runCatching {
                    clientFor(vendor).deletePerson(
                        DeletePersonAdapterRequest(externalIds = mapOf(vendor to externalId)),
                    )
                }.onFailure { e.addSuppressed(it) }
            }
            throw e
        }
        return AddPersonAdapterResponse(externalIds = merged)
    }

    override fun deletePerson(request: DeletePersonAdapterRequest) {
        request.externalIds.forEach { (vendor, externalId) ->
            clientFor(vendor).deletePerson(
                DeletePersonAdapterRequest(externalIds = mapOf(vendor to externalId)),
            )
        }
    }
}
