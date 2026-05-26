package com.alleslocker.backend.persistence.converter

import com.alleslocker.backend.application.common.security.CryptionService
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.springframework.context.annotation.Lazy

@Converter
class CryptionConverter(
    @Lazy private val cryptionService: CryptionService
) : AttributeConverter<String, String> {
    override fun convertToDatabaseColumn(attribute: String?): String? {
        if (attribute.isNullOrBlank()) {
            return null
        }
        return cryptionService.encrypt(attribute)
    }

    override fun convertToEntityAttribute(dbData: String?): String? {
        if (dbData.isNullOrBlank()) {
            return null
        }
        return cryptionService.decrypt(dbData)
    }
}