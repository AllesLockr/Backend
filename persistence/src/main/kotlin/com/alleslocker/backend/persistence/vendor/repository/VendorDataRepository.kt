package com.alleslocker.backend.persistence.vendor.repository

import com.alleslocker.backend.persistence.vendor.entity.VendorDataEntity
import org.springframework.data.jpa.repository.JpaRepository

interface VendorDataRepository : JpaRepository<VendorDataEntity, String> {
    fun findByForApi(forApi: String): VendorDataEntity?
}
