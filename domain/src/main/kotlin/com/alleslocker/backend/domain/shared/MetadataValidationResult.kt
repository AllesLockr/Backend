package com.alleslocker.backend.domain.shared

sealed class MetadataValidationResult(
    val message: String,
) {
    class Success : MetadataValidationResult(message = "Success")

    sealed class Error(
        message: String,
    ) : MetadataValidationResult(message) {
        class MissingRequiredMetadata(
            val missingFields: String,
            val forVendor: String,
        ) : Error(message = "The following metadata fields are required for $forVendor: $missingFields")

        class NoMetadataRequired(
            val forVendor: String,
        ) : Error(message = "No metadata fields required for $forVendor")

        class InvalidMetadata(
            val forVendor: String,
        ) : Error(message = "Invalid metadata fields for $forVendor")
    }
}
