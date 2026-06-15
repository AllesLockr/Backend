package com.alleslocker.backend.lockconnector.vendorspecificdefinitions

import com.alleslocker.backend.application.common.dto.MetadataEntryDto
import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitionsAdapter
import com.alleslocker.backend.domain.shared.MetadataValidationResult
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificDefinition
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificField
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificFieldType
import org.springframework.stereotype.Component

@Component
class VendorSpecificDefinitionsAdapterImpl : VendorSpecificDefinitionsAdapter {
    private val definitions =
        listOf(
            VendorSpecificDefinition(
                AvailableVendors.ISEO,
                listOf(
                    VendorSpecificField(name = "installer-email", type = VendorSpecificFieldType.EMAIL),
                    VendorSpecificField(name = "installer-password", type = VendorSpecificFieldType.PASSWORD),
                ),
            ),
        )

    override fun get(availableVendors: AvailableVendors): VendorSpecificDefinition? =
        definitions.find { it.vendorName == availableVendors }

    override fun validateMetadataRequest(
        forVendor: AvailableVendors,
        metadata: Set<MetadataEntryDto>?,
    ): MetadataValidationResult {
        val allowedFieldNames =
            get(forVendor)
                ?.vendorSpecificFields
                ?.map { it.name }
                ?.toSet()
                ?: emptySet()

        if (allowedFieldNames.isNotEmpty() && metadata == null) {
            return MetadataValidationResult.Error.MissingRequiredMetadata(allowedFieldNames.toString(), forVendor.name)
        }

        if (allowedFieldNames.isEmpty() && metadata != null) {
            return MetadataValidationResult.Error.NoMetadataRequired(forVendor.name)
        }

        val hasInvalidKeys = metadata!!.any { it.key !in allowedFieldNames }

        if (hasInvalidKeys) {
            return MetadataValidationResult.Error.InvalidMetadata(forVendor.name)
        }

        val providedKeys = metadata.map { it.key }.toSet()
        val missingFields = allowedFieldNames - providedKeys
        if (missingFields.isNotEmpty()) {
            return MetadataValidationResult.Error.MissingRequiredMetadata(missingFields.toString(), forVendor.name)
        }

        return MetadataValidationResult.Success()
    }
}
