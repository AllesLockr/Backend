package com.alleslocker.backend.persistence.vendor.adapter

import com.alleslocker.backend.application.vendor.gateway.VendorDataGateway
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.VendorData
import com.alleslocker.backend.domain.vendor.VendorId
import com.alleslocker.backend.persistence.vendor.mapper.toDomain
import com.alleslocker.backend.persistence.vendor.mapper.toEntity
import com.alleslocker.backend.persistence.vendor.repository.VendorDataRepository
import org.springframework.stereotype.Component

@Component
class VendorDataGatewayAdapter(
    private val repository: VendorDataRepository,
) : VendorDataGateway {
    override fun save(entity: VendorData): VendorData {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: VendorId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: VendorId): VendorData? = repository.findById(id.value).orElse(null)?.toDomain()

    override fun exists(id: VendorId): Boolean = repository.existsById(id.value)

    override fun findByForApi(forApi: AvailableVendors): VendorData? = repository.findByForApi(forApi.name)?.toDomain()

    override fun findAll(): List<VendorData> = repository.findAll().map { it.toDomain() }
}
