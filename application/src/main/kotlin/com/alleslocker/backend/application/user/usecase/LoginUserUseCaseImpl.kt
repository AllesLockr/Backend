package com.alleslocker.backend.application.user.usecase

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.OutputBoundary
import com.alleslocker.backend.application.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto
import com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway

internal class LoginUserUseCaseImpl(
    private val passwordHasher: com.alleslocker.backend.application.security.PasswordHasher,
    private val userGateway: com.alleslocker.backend.application.user.gateway.UserGateway
) : com.alleslocker.backend.application.user.usecase.LoginUserUseCase {

    override fun execute(
        request: com.alleslocker.backend.application.user.dto.request.LoginUserRequestDto,
        presenter: com.alleslocker.backend.application.common.OutputBoundary<com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto>
    ) {
        val user = userGateway.findByUsername(request.username)
        if (user == null) {
            presenter.presentFailure(_root_ide_package_.com.alleslocker.backend.application.common.ErrorResponse.NotFound("User doesn't exist"))
            return
        }

        if (!passwordHasher.verify(request.password, user.passwordHash.value)) {
            presenter.presentFailure(_root_ide_package_.com.alleslocker.backend.application.common.ErrorResponse.BadRequest("Invalid password"))
            return
        }

        presenter.present(
            _root_ide_package_.com.alleslocker.backend.application.user.dto.response.LoginUserResponseDto(
                user.id.value
            )
        )
    }
}