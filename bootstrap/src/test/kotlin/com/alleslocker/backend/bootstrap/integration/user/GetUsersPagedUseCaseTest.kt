package com.alleslocker.backend.bootstrap.integration.user

import com.alleslocker.backend.application.common.ErrorResponse
import com.alleslocker.backend.application.common.factory.UseCaseFactory
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.application.user.dto.request.GetUsersPagedRequestDto
import com.alleslocker.backend.application.user.dto.response.GetUsersPagedResponseDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.application.user.usecase.GetUsersPagedUseCase
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
class GetUsersPagedUseCaseTest(
    @Autowired private val useCaseFactory: UseCaseFactory,
    @Autowired private val userGateway: UserGateway,
    @Autowired private val userRepository: UserRepository,
    @Autowired private val passwordHasher: PasswordHasher,
) : FreeSpec({

        val useCase: GetUsersPagedUseCase = useCaseFactory.make(GetUsersPagedUseCase::class)

        beforeEach {
            userRepository.deleteAll()
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
            userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.ADMIN,
                    firstname = UserFirstname("Anna"),
                    lastname = UserLastname("Admin"),
                    username = Username("aadmin"),
                    email = UserEmail("anna@test.de"),
                    passwordHash = PasswordHash(passwordHasher.hash("pass456")),
                    isActive = true,
                    mustChangePassword = false,
                ),
            )
            userGateway.save(
                User(
                    id = UserId.generate(),
                    role = UserRole.USER,
                    firstname = UserFirstname("Tom"),
                    lastname = UserLastname("Test"),
                    username = Username("ttest"),
                    email = UserEmail("tom@test.de"),
                    passwordHash = PasswordHash(passwordHasher.hash("pass789")),
                    isActive = false,
                    mustChangePassword = true,
                ),
            )
        }

        "should return all users on page 0 with size 10" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(),
                    page = 0,
                    size = 10,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response shouldNotBe null
            presenter.response!!
                .page.content.size shouldBe 3
            presenter.response!!.page.totalElements shouldBe 3
            presenter.response!!.page.totalPages shouldBe 1
        }

        "should paginate correctly" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(),
                    page = 0,
                    size = 1,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!
                .page.content.size shouldBe 1
            presenter.response!!.page.totalElements shouldBe 3
            presenter.response!!.page.totalPages shouldBe 3
            presenter.response!!.page.hasNext() shouldBe true
        }

        "should filter by search term (firstname)" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(search = "Max"),
                    page = 0,
                    size = 10,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!
                .page.content.size shouldBe 1
            presenter.response!!
                .page.content[0]
                .username shouldBe "mmuster"
        }

        "should filter by search term (email)" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(search = "anna@test.de"),
                    page = 0,
                    size = 10,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!
                .page.content.size shouldBe 1
            presenter.response!!
                .page.content[0]
                .username shouldBe "aadmin"
        }

        "should filter by search term (lastname)" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(search = "Mustermann"),
                    page = 0,
                    size = 10,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!
                .page.content.size shouldBe 1
            presenter.response!!
                .page.content[0]
                .username shouldBe "mmuster"
        }

        "should return empty page when filter matches nothing" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(search = "xxxxxxxxx"),
                    page = 0,
                    size = 10,
                ),
                presenter,
            )

            presenter.error shouldBe null
            presenter.response!!
                .page.content.size shouldBe 0
            presenter.response!!.page.totalElements shouldBe 0
        }

        "should reject negative page" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(),
                    page = -1,
                    size = 10,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject size of 0" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(),
                    page = 0,
                    size = 0,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }

        "should reject size greater than 500" {
            val presenter = TestPresenter<GetUsersPagedResponseDto>()

            useCase.execute(
                GetUsersPagedRequestDto(
                    requesterId = "",
                    filter = UserFilterDto(),
                    page = 0,
                    size = 501,
                ),
                presenter,
            )

            presenter.response shouldBe null
            presenter.error shouldNotBe null
            presenter.error!!.shouldBeInstanceOf<ErrorResponse.BadRequest>()
        }
    })
