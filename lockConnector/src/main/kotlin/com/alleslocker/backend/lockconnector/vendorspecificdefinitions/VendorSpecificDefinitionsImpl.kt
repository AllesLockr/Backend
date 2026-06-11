package com.alleslocker.backend.lockconnector.vendorspecificdefinitions

import com.alleslocker.backend.application.vendor.adapter.VendorSpecificDefinitions
import com.alleslocker.backend.domain.vendor.AvailableVendors
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificDefinition
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificField
import com.alleslocker.backend.domain.vendor.definition.VendorSpecificFieldType
import org.springframework.stereotype.Component

@Component
class VendorSpecificDefinitionsImpl : VendorSpecificDefinitions {
    private val definitions =
        listOf(
            VendorSpecificDefinition(
                AvailableVendors.ISEO,
                listOf(
                    VendorSpecificField(name = "installer-email", type = VendorSpecificFieldType.EMAIL),
                ),
            ),
        )

    override fun get(availableVendors: AvailableVendors): VendorSpecificDefinition? =
        definitions.find { it.vendorName == availableVendors }
}
