package com.alleslocker.backend.persistence.vendor.mapper

import com.alleslocker.backend.domain.vendor.ApiPassword
import com.alleslocker.backend.domain.vendor.ApiUsername
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorAuthentication
import com.alleslocker.backend.domain.vendor.VendorAuthentication.BaseAuth
import com.alleslocker.backend.domain.vendor.VendorConnectionState
import com.alleslocker.backend.domain.vendor.VendorData
import com.alleslocker.backend.domain.vendor.VendorId
import com.alleslocker.backend.domain.vendor.VendorState
import com.alleslocker.backend.persistence.shared.mapper.toDomain
import com.alleslocker.backend.persistence.shared.mapper.toEntity
import com.alleslocker.backend.persistence.vendor.entity.VendorDataEntity
import java.net.URI

fun VendorDataEntity.toDomain(): VendorData {
    val auth =
        if (!this.apiKey.isNullOrBlank()) {
            VendorAuthentication.ApiKey(this.apiKey!!)
        } else if (!this.apiUsername.isNullOrBlank() && !this.apiPassword.isNullOrBlank()) {
            BaseAuth(
                ApiUsername(this.apiUsername!!),
                ApiPassword(this.apiPassword!!),
            )
        } else {
            throw IllegalStateException("Could not determine auth state for api-data db entry: ${this.id}")
        }

    return VendorData(
        id = VendorId(this.id),
        forVendor = AvailableVendors.valueOf(this.forApi),
        baseUrl = URI(this.baseUrl),
        vendorAuthentication = auth,
        vendorState = VendorState(VendorConnectionState.valueOf(vendorConnectionState), lastChecked),
        metadata = metadata.map { it.toDomain() }.toSet(),
    )
}

fun VendorData.toEntity(existing: VendorDataEntity? = null): VendorDataEntity {
    val entity = existing ?: VendorDataEntity()

    entity.id = id.value
    entity.forApi = forVendor.toString()
    entity.baseUrl = baseUrl.toString()
    entity.lastChecked = vendorState.lastChecked
    entity.vendorConnectionState = vendorState.connectionState.toString()
    entity.metadata = metadata.map { it.toEntity() }.toMutableSet()

    when (val auth = vendorAuthentication) {
        is VendorAuthentication.ApiKey -> {
            entity.apiKey = auth.value
            entity.apiUsername = null
            entity.apiPassword = null
        }

        is BaseAuth -> {
            entity.apiKey = null
            entity.apiUsername = auth.username.value
            entity.apiPassword = auth.password.value
        }
    }

    return entity
}
