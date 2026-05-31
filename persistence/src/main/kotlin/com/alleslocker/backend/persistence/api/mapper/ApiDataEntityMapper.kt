package com.alleslocker.backend.persistence.api.mapper

import com.alleslocker.backend.domain.api.ApiAuthentication
import com.alleslocker.backend.domain.api.ApiData
import com.alleslocker.backend.domain.api.ApiId
import com.alleslocker.backend.domain.api.ApiPassword
import com.alleslocker.backend.domain.api.ApiUsername
import com.alleslocker.backend.domain.api.AvailableApis
import com.alleslocker.backend.persistence.api.entity.ApiDataEntity
import java.net.URI

fun ApiDataEntity.toDomain(): ApiData {
    val auth =
        if (!this.apiKey.isNullOrBlank()) {
            ApiAuthentication.ApiKey(this.apiKey!!)
        } else if (!this.apiUsername.isNullOrBlank() && !this.apiPassword.isNullOrBlank()) {
            ApiAuthentication.BaseAuth(
                ApiUsername(this.apiUsername!!),
                ApiPassword(this.apiPassword!!),
            )
        } else {
            throw IllegalStateException("Could not determine auth state for api-data db entry: ${this.id}")
        }

    return ApiData(
        id = ApiId(this.id),
        forApi = AvailableApis.valueOf(this.forApi),
        baseUrl = URI(this.baseUrl),
        apiAuthentication = auth,
    )
}

fun ApiData.toEntity(existing: ApiDataEntity? = null): ApiDataEntity {
    val entity = existing ?: ApiDataEntity()

    entity.id = id.value
    entity.forApi = forApi.toString()
    entity.baseUrl = baseUrl.toString()

    when (val auth = apiAuthentication) {
        is ApiAuthentication.ApiKey -> {
            entity.apiKey = auth.value
            entity.apiUsername = null
            entity.apiPassword = null
        }

        is ApiAuthentication.BaseAuth -> {
            entity.apiKey = null
            entity.apiUsername = auth.username.value
            entity.apiPassword = auth.password.value
        }
    }

    return entity
}
