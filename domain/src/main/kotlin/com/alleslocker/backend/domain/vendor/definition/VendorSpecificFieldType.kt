package com.alleslocker.backend.domain.vendor.definition

import com.alleslocker.backend.domain.shared.Email

enum class VendorSpecificFieldType {
    EMAIL,
    PASSWORD,
    TEXT,
    NUMBER,
    ;

    fun isValid(value: String): Boolean =
        when (this) {
            EMAIL -> Email.isValid(value)
            NUMBER -> value.toBigDecimalOrNull() != null
            TEXT, PASSWORD -> value.isNotBlank()
        }
}
