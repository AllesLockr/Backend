package com.alleslocker.backend.persistence.api.adapter

import com.alleslocker.backend.application.api.gateway.ApiDataGateway
import com.alleslocker.backend.domain.api.ApiData
import com.alleslocker.backend.domain.api.ApiId
import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.persistence.api.mapper.toDomain
import com.alleslocker.backend.persistence.api.mapper.toEntity
import com.alleslocker.backend.persistence.api.repository.ApiDataRepository
import org.springframework.stereotype.Component

@Component
class ApiDataGatewayAdapter(private val repository: ApiDataRepository) : ApiDataGateway {
    override fun save(entity: ApiData): ApiData {
        val existing = repository.findById(entity.id.value).orElse(null)
        return repository.save(entity.toEntity(existing)).toDomain()
    }

    override fun deleteById(id: ApiId) {
        repository.deleteById(id.value)
    }

    override fun findById(id: ApiId): ApiData? {
        return repository.findById(id.value).orElse(null)?.toDomain()
    }

    override fun exists(id: ApiId): Boolean {
        return repository.existsById(id.value)
    }

    override fun findByForApi(forApi: AvailableApis): ApiData? {
        return repository.findByForApi(forApi.name)?.toDomain()
    }
}