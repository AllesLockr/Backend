package eu.luftiger.cae.application.user.usecase

import eu.luftiger.cae.application.common.ErrorResponse
import eu.luftiger.cae.application.common.OutputBoundary
import eu.luftiger.cae.application.security.PasswordHasher
import eu.luftiger.cae.application.user.dto.request.LoginUserRequestDto
import eu.luftiger.cae.application.user.dto.response.LoginUserResponseDto
import eu.luftiger.cae.application.user.gateway.UserGateway

internal class LoginUserUseCaseImpl(
    private val passwordHasher: PasswordHasher,
    private val userGateway: UserGateway
) : LoginUserUseCase {

    override fun execute(
        request: LoginUserRequestDto,
        presenter: OutputBoundary<LoginUserResponseDto>
    ) {
        val user = userGateway.findByUsername(request.username)
        if (user == null) {
            presenter.presentFailure(ErrorResponse.NotFound("User doesn't exist"))
            return
        }

        if (!passwordHasher.verify(request.password, user.passwordHash.value)) {
            presenter.presentFailure(ErrorResponse.BadRequest("Invalid password"))
            return
        }

        presenter.present(LoginUserResponseDto(user.id.value))
    }
}