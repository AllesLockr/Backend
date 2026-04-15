package eu.luftiger.cae.application.user.usecase

import eu.luftiger.cae.application.common.ErrorResponse
import eu.luftiger.cae.application.common.OutputBoundary
import eu.luftiger.cae.application.security.PasswordHasher
import eu.luftiger.cae.application.user.dto.request.RegisterUserRequestDto
import eu.luftiger.cae.application.user.dto.response.RegisterUserResponseDto
import eu.luftiger.cae.application.user.gateway.UserGateway
import eu.luftiger.cae.domain.user.PasswordHash
import eu.luftiger.cae.domain.user.User
import eu.luftiger.cae.domain.user.UserId
import eu.luftiger.cae.domain.user.Username

internal class RegisterUserUseCaseImpl(
    private val passwordHasher: PasswordHasher,
    private val userGateway: UserGateway
) : RegisterUserUseCase {

    override fun execute(
        request: RegisterUserRequestDto,
        presenter: OutputBoundary<RegisterUserResponseDto>
    ) {
        if (userGateway.findByUsername(request.username) != null) {
            presenter.presentFailure(ErrorResponse.AlreadyExists("User with username: ${request.username} already exists."))
            return
        }

        val hashedPassword = passwordHasher.hash(request.password)
        val user = try {
            User(
                id = UserId.generate(),
                username = Username(request.username),
                passwordHash = PasswordHash(hashedPassword)
            )
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.BadRequest(e.message ?: "Invalid user data"))
            return
        }

        val save = try {
            userGateway.save(user)
        } catch (e: Exception) {
            presenter.presentFailure(ErrorResponse.InternalServerError("Failed to save user: ${e.message}"))
            return
        }

        presenter.present(RegisterUserResponseDto(save.id.value))
    }
}