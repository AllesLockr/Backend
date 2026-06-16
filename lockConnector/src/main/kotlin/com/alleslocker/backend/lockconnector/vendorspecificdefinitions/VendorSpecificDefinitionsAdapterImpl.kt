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
                    VendorSpecificField(
                        name = "Installer Email",
                        type = VendorSpecificFieldType.EMAIL,
                        internal = false,
                        description = "An installer account is required to add new locks to the platform.",
                    ),
                    VendorSpecificField(
                        name = "Installer Password",
                        type = VendorSpecificFieldType.PASSWORD,
                        internal = false,
                        description = "Password for the installer account.",
                    ),
                    VendorSpecificField(
                        name = "installer-account-id",
                        type = VendorSpecificFieldType.NUMBER,
                        internal = true,
                        description = "ID is required to modify the installer account.",
                    ),
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
                ?.filter { it.internal == false }
                ?.map { it.name }
                ?.toSet()
                ?: emptySet()

        if (allowedFieldNames.isEmpty()) {
            return if (metadata == null) {
                MetadataValidationResult.Success()
            } else {
                MetadataValidationResult.Error.NoMetadataRequired(forVendor.name)
            }
        }

        if (metadata == null) {
            return MetadataValidationResult.Error.MissingRequiredMetadata(allowedFieldNames.toString(), forVendor.name)
        }

        val providedKeys = metadata.map { it.key }.toSet()
        val missingFields = allowedFieldNames - providedKeys
        if (missingFields.isNotEmpty()) {
            return MetadataValidationResult.Error.MissingRequiredMetadata(missingFields.toString(), forVendor.name)
        }

        return MetadataValidationResult.Success()
    }
}
