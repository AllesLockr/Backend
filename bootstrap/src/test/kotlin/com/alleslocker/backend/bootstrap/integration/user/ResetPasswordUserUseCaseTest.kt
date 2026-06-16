package com.alleslocker.backend.bootstrap.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.ResetPasswordUserRequestDto
import com.alleslocker.backend.application.user.dto.response.ResetPasswordUserResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.ResetPasswordUserUseCase
import com.alleslocker.backend.bootstrap.integration.config.TestPresenter
import com.alleslocker.backend.bootstrap.integration.config.TestUserIntegrationConfig
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import com.alleslocker.backend.persistence.user.repository.UserRepository
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestUserIntegrationConfig::class])
@ActiveProfiles("test")
class ResetPasswordUserUseCaseTest(
    @Autowired private val useCaseFactory: UseCaseFactory,
    @Autowired private val userGateway: UserGateway,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordHasher: PasswordHasher,
) : FreeSpec({

        val useCase: ResetPasswordUserUseCase =
            useCaseFactory.make(ResetPasswordUserUseCase::class)

        lateinit var userId: String

        beforeEach {
            userRepository.deleteAll()
            val saved =
                userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Max"),
                        lastname = UserLastname("Mustermann"),
                        username = Username("mmuster"),
                        email = UserEmail("max@test.de"),
                        passwordHash = PasswordHash(passwordHasher.hash("oldPass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            userId = saved.id.value
        }

        "should reset password with correct old password" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = userId,
                    oldPassword = "oldPass123",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!.userId shouldBe userId

            val updatedUser = userGateway.findById(UserId(userId))
            updatedUser!!.mustChangePassword shouldBe false
            passwordHasher.verify("newPass456", updatedUser.passwordHash.value) shouldBe true
            passwordHasher.verify("oldPass123", updatedUser.passwordHash.value) shouldBe false
        }

        "should reject wrong old password" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = userId,
                    oldPassword = "wrongPassword",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
            presenter.error!!.message shouldBe "Invalid old password"
        }

        "should reject reset for non-existent user" {
            val presenter = TestPresenter<ResetPasswordUserResponseDto>()

            useCase.execute(
                ResetPasswordUserRequestDto(
                    requestorId = "non-existent-id",
                    oldPassword = "oldPass123",
                    newPassword = "newPass456",
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
