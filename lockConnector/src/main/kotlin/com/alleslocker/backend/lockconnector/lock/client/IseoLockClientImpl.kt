package com.alleslocker.backend.lockconnector.lock.client

import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.application.lock.dto.response.FetchedLockDto
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.lock.LockId
import com.alleslocker.backend.domain.lock.LockName
import com.alleslocker.backend.domain.lock.LockSerialNumber
import com.alleslocker.backend.domain.shared.MetadataEntry
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.ExternalApiIdentity
import com.alleslocker.backend.domain.vendor.ExternalId
import com.alleslocker.backend.lockconnector.auth.common.TokenProvider
import com.alleslocker.backend.lockconnector.auth.config.ConfigProvider
import com.alleslocker.backend.lockconnector.common.GenericRestClient
import com.alleslocker.backend.lockconnector.lock.adapter.LockClient
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.client.body

class IseoLockClientImpl(
    private val restClient: GenericRestClient,
    private val tokenProvider: TokenProvider,
    private val configProvider: ConfigProvider,
) : LockClient {
    private val lockCreationStepKeyName = "iseo-create-lock-step"
    private val lockTagIdKeyName = "lockTag-id"

    private data class IseoTag(
        val id: Long,
    )

    private data class IseoSmartLockItem(
        val id: Int,
        val name: String,
        val serialNumber: String,
        val tags: List<IseoTag> = emptyList(),
    )

    private data class IseoSmartLocksPage(
        val content: List<IseoSmartLockItem>,
        val last: Boolean,
        val number: Int,
    )

    override fun fetchAllLocks(): FetchLocksAdapterResponse {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl
        val locks = mutableListOf<FetchedLockDto>()
        var page = 0

        do {
            val response =
                restClient.get<IseoSmartLocksPage>(
                    endpoint = "$baseUrl/api/v2/smartLocks?page=$page&size=100",
                    headers = mapOf("Authorization" to "Bearer $token"),
                ) ?: break

            response.content.forEach { item ->
                locks +=
                    FetchedLockDto(
                        name = item.name,
                        serialNumber = item.serialNumber,
                        tagId = item.tags.firstOrNull()?.id,
                        vendor = AvailableVendors.ISEO,
                        externalId = item.serialNumber,
                    )
            }

            if (response.last) break
            page++
        } while (true)

        return FetchLocksAdapterResponse(locks = locks)
    }

    override fun createLock(forVendor: AvailableVendors): Lock {
        val metadata =
            setOf(MetadataEntry(key = lockCreationStepKeyName, value = IseoLockCreationStep.INSTALLER_MODE.name))

        setInstallerMode(true)

        return Lock(
            id = LockId.generate(),
            name = LockName("$forVendor"),
            serialNumber = LockSerialNumber("creation in progress!"),
            metadata = metadata,
            apiIdentity =
                ExternalApiIdentity(
                    api = forVendor,
                    externalId = ExternalId(""),
                ),
        )
    }

    override fun updateLock(lock: Lock): Lock {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl
        val headers = mapOf("Authorization" to "Bearer $token")

        val creationStep = lock.metadata.find { it.key == lockCreationStepKeyName }

        if (creationStep != null && creationStep.value == IseoLockCreationStep.INSTALLER_MODE.name) {
            val getLocksResponse =
                restClient.get<IseoSmartLocksPage>(
                    endpoint = "$baseUrl/api/v2/smartLocks/",
                    headers = headers,
                ) ?: throw Exception("Could not get smart locks to continue with add lock process!")

            // Response is sorted by ID DESC, therefore the first item of the collection should be the newest.
            val lockAddedByInstallerApp = getLocksResponse.content.first()

            // Create lock tag
            val createLockTagResponse =
                restClient
                    .postForResponse(
                        endpoint = "$baseUrl/api/v2/smartLockTags",
                        body =
                            mapOf(
                                "extId" to lock.id.value,
                                "type" to "alles-locker",
                                "name" to lock.id.value,
                                "color" to "#673ab7",
                            ),
                        headers = headers,
                        contentType = APPLICATION_JSON,
                    ).body<LockTagResponse>()

            val lockTagId = createLockTagResponse?.id ?: throw Exception("Could not create lock tag!")

            // Attach created lock tag to iseo
            restClient
                .putForResponse(
                    endpoint = "$baseUrl/api/v2/smartLocks/${lockAddedByInstallerApp.id}",
                    body =
                        mapOf(
                            "lockTagIds" to listOf(lockTagId),
                        ),
                    headers = headers,
                    contentType = APPLICATION_JSON,
                ).toBodilessEntity()

            val updatedLock =
                lock.copy(
                    name = LockName(lockAddedByInstallerApp.name),
                    serialNumber = LockSerialNumber(lockAddedByInstallerApp.serialNumber),
                    apiIdentity =
                        ExternalApiIdentity(
                            AvailableVendors.ISEO,
                            ExternalId(lockAddedByInstallerApp.id.toString()),
                        ),
                    metadata =
                        setOf(
                            MetadataEntry(lockCreationStepKeyName, IseoLockCreationStep.COMPLETED.name),
                            MetadataEntry(lockTagIdKeyName, lockTagId.toString()),
                        ),
                )

            setInstallerMode(false)

            return updatedLock
        } else {
            setInstallerMode(true)
            val updateLockResponseCode =
                restClient
                    .putForResponse(
                        endpoint = "$baseUrl/api/v2/smartLocks/${lock.apiIdentity!!.externalId.value}",
                        body =
                            mapOf(
                                "name" to lock.name,
                            ),
                        headers = headers,
                        contentType = APPLICATION_JSON,
                    ).toBodilessEntity()
                    .statusCode

            setInstallerMode(false)

            if (updateLockResponseCode != HttpStatus.OK) {
                throw Exception("Could not update lock!")
            }

            return lock
        }
    }

    private fun setInstallerMode(value: Boolean) {
        val token = tokenProvider.getValidToken()
        val baseUrl = configProvider.load(AvailableVendors.ISEO).baseUrl

        val requestBody =
            mapOf(
                "installationMode" to value.toString(),
            )

        restClient.putForResponse(
            endpoint = "$baseUrl/api/v2/companies",
            body = requestBody,
            headers = mapOf("Authorization" to "Bearer $token"),
            contentType = APPLICATION_JSON,
        )
    }

    enum class IseoLockCreationStep {
        INSTALLER_MODE,
        COMPLETED,
    }

    private data class LockTagResponse(
        val id: Int,
        val name: String?,
        val color: String?,
        val type: String?,
        val extId: String?,
        val hidden: Boolean?,
    )
}
