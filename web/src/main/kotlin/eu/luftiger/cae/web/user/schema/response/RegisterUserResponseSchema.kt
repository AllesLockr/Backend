package eu.luftiger.cae.web.user.schema.response

data class RegisterUserResponseSchema(
    val userId: String,
    val jwtToken: String
)