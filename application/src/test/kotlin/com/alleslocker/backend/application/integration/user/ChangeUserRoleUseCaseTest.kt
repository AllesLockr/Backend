package com.alleslocker.backend.application.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.integration.config.TestPresenter
import com.alleslocker.backend.application.integration.config.createUserTestContext
import com.alleslocker.backend.application.user.dto.UserRoleDto
import com.alleslocker.backend.application.user.dto.request.ChangeUserRoleRequestDto
import com.alleslocker.backend.application.user.usecase.ChangeUserRoleUseCase
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.types.shouldBeInstanceOf

class ChangeUserRoleUseCaseTest :
    FreeSpec({
        val ctx = createUserTestContext()
        val useCase: ChangeUserRoleUseCase = ctx.useCaseFactory.make(ChangeUserRoleUseCase::class)

        lateinit var adminId: String
        lateinit var targetUserId: String

        beforeEach {
            ctx.userGateway.deleteAll()
            ctx.logger.clear()
            val admin =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.ADMIN,
                        firstname = UserFirstname("Admin"),
                        lastname = UserLastname("User"),
                        username = Username("admin"),
                        email = UserEmail("admin@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("admin123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            adminId = admin.id.value

            val targetUser =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Max"),
                        lastname = UserLastname("Mustermann"),
                        username = Username("mmuster"),
                        email = UserEmail("max@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )
            targetUserId = targetUser.id.value
        }

        "should change USER to ADMIN" {
            val presenter = TestPresenter<Unit>()

            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    role = UserRoleDto.ADMIN,
                ),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.role shouldBe UserRole.ADMIN
            ctx.logger.auditLogs.size shouldBe 1
        }

        "should change ADMIN to USER" {
            ctx.userGateway.save(
                ctx.userGateway.findById(UserId(targetUserId))!!.copy(role = UserRole.ADMIN),
            )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    role = UserRoleDto.USER,
                ),
                presenter,
            )

            presenter.error shouldBe null
            val updatedUser = ctx.userGateway.findById(UserId(targetUserId))
            updatedUser!!.role shouldBe UserRole.USER
        }

        "should reject setting the same role" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = adminId,
                    userId = targetUserId,
                    role = UserRoleDto.USER,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
            presenter.error!!.message shouldContain "already this role"
        }

        "should reject role change by non-admin" {
            val regularUser =
                ctx.userGateway.save(
                    User(
                        id = UserId.generate(),
                        role = UserRole.USER,
                        firstname = UserFirstname("Regular"),
                        lastname = UserLastname("User"),
                        username = Username("regular"),
                        email = UserEmail("regular@test.de"),
                        passwordHash = PasswordHash(ctx.passwordHasher.hash("pass123")),
                        isActive = true,
                        mustChangePassword = false,
                    ),
                )

            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = regularUser.id.value,
                    userId = targetUserId,
                    role = UserRoleDto.ADMIN,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.Unauthorized>()
        }

        "should reject role change of non-existent user" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = adminId,
                    userId = "non-existent-id",
                    role = UserRoleDto.ADMIN,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }

        "should reject role change with non-existent requestor" {
            val presenter = TestPresenter<Unit>()
            useCase.execute(
                ChangeUserRoleRequestDto(
                    requestorId = "non-existent-id",
                    userId = targetUserId,
                    role = UserRoleDto.ADMIN,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.NotFound>()
        }
    })
