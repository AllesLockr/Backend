package com.alleslocker.backend.domain.vendor

import java.time.Instant

data class VendorState(
    val connectionState: VendorConnectionState,
    val lastChecked: Instant,
)
