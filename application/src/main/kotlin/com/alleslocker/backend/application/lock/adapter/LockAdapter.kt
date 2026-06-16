package com.alleslocker.backend.application.lock.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse
import com.alleslocker.backend.domain.lock.Lock
import com.alleslocker.backend.domain.vendor.AvailableVendors

interface LockAdapter : Adapter {
    fun fetchAllLocks(): FetchLocksAdapterResponse

    fun createLock(forVendor: AvailableVendors): Lock

    fun updateLock(lock: Lock): Lock
}
