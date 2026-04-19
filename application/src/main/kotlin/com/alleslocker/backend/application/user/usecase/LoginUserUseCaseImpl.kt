package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway

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

        presenter.present(
            LoginUserResponseDto(
                user.id.value
            )
        )
    }
}