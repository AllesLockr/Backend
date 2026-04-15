package eu.luftiger.cae.web.user.schema.response

data class LoginUserResponseSchema(
    val userId: String,
    val jwtToken: String
)