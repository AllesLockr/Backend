package com.alleslocker.backend.application.lock.adapter

import com.alleslocker.backend.application.common.adapter.Adapter
import com.alleslocker.backend.application.lock.dto.response.FetchLocksAdapterResponse

interface LockAdapter : Adapter {
    fun fetchAllLocks(): FetchLocksAdapterResponse
}
