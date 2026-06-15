package com.alleslocker.backend.bootstrap.infrastructure

import com.alleslocker.backend.application.common.Logger
import com.alleslocker.backend.application.common.security.PasswordHasher
import com.alleslocker.backend.application.common.service.PasswordGeneratorService
import com.alleslocker.backend.application.user.dto.filter.UserFilterDto
import com.alleslocker.backend.application.user.gateway.UserGateway
import com.alleslocker.backend.domain.user.PasswordHash
import com.alleslocker.backend.domain.user.User
import com.alleslocker.backend.domain.user.UserEmail
import com.alleslocker.backend.domain.user.UserFirstname
import com.alleslocker.backend.domain.user.UserId
import com.alleslocker.backend.domain.user.UserLastname
import com.alleslocker.backend.domain.user.UserRole
import com.alleslocker.backend.domain.user.Username
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataSeeder(
    private val userGateway: UserGateway,
    private val passwordHasher: PasswordHasher,
    private val logger: Logger,
) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val existingUsers = userGateway.getAllUsersPaged(UserFilterDto(), 0, 1)
        if (existingUsers.totalElements > 0) {
            logger.info("Users already exist in the database, skipping seeding")
            return
        }
        val username = "Admin"
        val passwordGenerator = PasswordGeneratorService()
        val rawPassword = passwordGenerator.generate(length = 16)
        println("*** INITIAL ADMIN PASSWORD: $rawPassword — save this now, it will not be shown again ***")
        logger.info("Creating initial admin user '$username'; password generated and stored securely")
        val user =
            User(
                id = UserId.generate(),
                role = UserRole.ADMIN,
                firstname = UserFirstname("Admin"),
                lastname = UserLastname("Admin"),
                username = Username(username),
                email = UserEmail("admin@admin.com"),
                passwordHash = PasswordHash(passwordHasher.hash(rawPassword)),
                isActive = true,
                mustChangePassword = true,
            )
        userGateway.save(user)
        logger.info("Initial admin user '$username' created. Change this password on first login.")
    }
}
