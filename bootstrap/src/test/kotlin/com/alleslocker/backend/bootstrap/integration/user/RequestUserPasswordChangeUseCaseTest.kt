package com.alleslocker.backend.bootstrap.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.request.RequestUserPasswordChangeRequestDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.RequestUserPasswordChangeUseCase
import com.alleslocker.backend.bootstrap.integration.config.TestLogger
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
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [TestUserIntegrationConfig::class])
@ActiveProfiles("test")
class RequestUserPasswordChangeUseCaseTest(
    @Autowired private val useCaseFactory: UseCaseFactory,
    @Autowired private val userGateway: UserGateway,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordHasher: PasswordHasher,
    @Autowired private val testLogger: TestLogger,
) : FreeSpec({

        val useCase: RequestUserPasswordChangeUseCase =
            useCaseFactory.make(RequestUserPasswordChangeUseCase::class)

        lateinit var adminId: String
        lateinit var targetUserId: String

        beforeEach {
            userRepository.deleteAll()
            testLogger.clear()
            val admin =
                userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.ADMIN,
                        firstname = UserFirstname("Admin"),
                        lastname = UserLastname("User"),
                        username = Username("admin"),
                        email = UserEmail("admin@test.de"),
                        passwordHash = PasswordHash(passwordHasher.hash("admin123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            adminId = admin.id.value

            val targetUser =
                userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Max"),
                        lastname = UserLastname("Mustermann"),
                        username = Username("mmuster"),
                        email = UserEmail("max@test.de"),
                        passwordHash = PasswordHash(passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            targetUserId = targetUser.id.value
        }

        "should request password change for user" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                RequestUserPasswordChangeRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = userGateway.findById(UserId(targetUserId))
            updatedUser!!.mustChangePassword shouldBe true
            testLogger.auditLogs.size shouldBe 1
        }

        "should reject if user already requested to change password" {
            userGateway.save(
                userGateway.findById(UserId(targetUserId))!!.copy(mustChangePassword = true),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                RequestUserPasswordChangeRequestDto(requestorId = adminId, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
            presenter.error!!.message shouldContain "already requested"
        }

        "should reject request by non-admin" {
            val regularUser =
                userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Regular"),
                        lastname = UserLastname("User"),
                        username = Username("regular"),
                        email = UserEmail("regular@test.de"),
                        passwordHash = PasswordHash(passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                RequestUserPasswordChangeRequestDto(requestorId = regularUser.id.value, userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject request for non-existent user" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                RequestUserPasswordChangeRequestDto(requestorId = adminId, userId = "non-existent-id"),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject request with non-existent requestor" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                RequestUserPasswordChangeRequestDto(requestorId = "non-existent-id", userId = targetUserId),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
