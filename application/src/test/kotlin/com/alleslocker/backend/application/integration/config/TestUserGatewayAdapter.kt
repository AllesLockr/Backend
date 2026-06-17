package com.alleslocker.backend.application.integration.config

import com.alleslocker.backend.application.common.model.Page
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
import java.sql.Connection
import java.sql.DriverManager

class TestUserGatewayAdapter : UserGateway {
    private val connection: Connection = DriverManager.getConnection(JDBC_URL)

    init {
        createTable()
    }

    fun deleteAll() {
        connection.createStatement().use { stmt ->
            stmt.execute("""DELETE FROM "user" """)
        }
    }

    private fun createTable() {
        connection.createStatement().use { stmt ->
            stmt.execute(
                """
                CREATE TABLE IF NOT EXISTS "user" (
                    id VARCHAR(255) PRIMARY KEY,
                    role VARCHAR(50) NOT NULL,
                    firstname VARCHAR(100) NOT NULL,
                    lastname VARCHAR(100) NOT NULL,
                    username VARCHAR(100) NOT NULL UNIQUE,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    is_active BOOLEAN NOT NULL,
                    must_change_password BOOLEAN NOT NULL
                )
                """.trimIndent(),
            )
        }
    }

    override fun save(entity: User): User {
        val existing = findById(entity.id)
        if (existing != null) {
            connection
                .prepareStatement(
                    """
                    UPDATE "user" SET role=?, firstname=?, lastname=?, username=?, email=?, password_hash=?, is_active=?, must_change_password=?
                    WHERE id=?
                    """.trimIndent(),
                ).use { stmt ->
                    stmt.setString(1, entity.role.name)
                    stmt.setString(2, entity.firstname.value)
                    stmt.setString(3, entity.lastname.value)
                    stmt.setString(4, entity.username.value)
                    stmt.setString(5, entity.email.value)
                    stmt.setString(6, entity.passwordHash.value)
                    stmt.setBoolean(7, entity.isActive)
                    stmt.setBoolean(8, entity.mustChangePassword)
                    stmt.setString(9, entity.id.value)
                    stmt.executeUpdate()
                }
        } else {
            connection
                .prepareStatement(
                    """
                    INSERT INTO "user" (id, role, firstname, lastname, username, email, password_hash, is_active, must_change_password)
                    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent(),
                ).use { stmt ->
                    stmt.setString(1, entity.id.value)
                    stmt.setString(2, entity.role.name)
                    stmt.setString(3, entity.firstname.value)
                    stmt.setString(4, entity.lastname.value)
                    stmt.setString(5, entity.username.value)
                    stmt.setString(6, entity.email.value)
                    stmt.setString(7, entity.passwordHash.value)
                    stmt.setBoolean(8, entity.isActive)
                    stmt.setBoolean(9, entity.mustChangePassword)
                    stmt.executeUpdate()
                }
        }
        return entity
    }

    override fun deleteById(id: UserId) {
        connection.prepareStatement("""DELETE FROM "user" WHERE id=?""").use { stmt ->
            stmt.setString(1, id.value)
            stmt.executeUpdate()
        }
    }

    override fun findById(id: UserId): User? {
        connection.prepareStatement("""SELECT * FROM "user" WHERE id=?""").use { stmt ->
            stmt.setString(1, id.value)
            stmt.executeQuery().use { rs ->
                if (rs.next()) return mapRow(rs)
            }
        }
        return null
    }

    override fun exists(id: UserId): Boolean = findById(id) != null

    override fun findByUsername(username: String): User? {
        connection.prepareStatement("""SELECT * FROM "user" WHERE username=?""").use { stmt ->
            stmt.setString(1, username)
            stmt.executeQuery().use { rs ->
                if (rs.next()) return mapRow(rs)
            }
        }
        return null
    }

    override fun findByEmail(email: String): User? {
        connection.prepareStatement("""SELECT * FROM "user" WHERE email=?""").use { stmt ->
            stmt.setString(1, email)
            stmt.executeQuery().use { rs ->
                if (rs.next()) return mapRow(rs)
            }
        }
        return null
    }

    override fun existsByEmail(email: String): Boolean = findByEmail(email) != null

    override fun existsByUsername(username: String): Boolean = findByUsername(username) != null

    override fun getAllUsersPaged(
        filter: UserFilterDto,
        page: Int,
        size: Int,
    ): Page<User> {
        val search = filter.search
        val whereClause =
            if (search != null) {
                "WHERE LOWER(firstname) LIKE ? OR LOWER(lastname) LIKE ? OR LOWER(email) LIKE ? OR LOWER(username) LIKE ?"
            } else {
                ""
            }

        val totalElements =
            connection.prepareStatement("""SELECT COUNT(*) FROM "user" $whereClause""").use { countStmt ->
                if (search != null) {
                    val pattern = "%${search.lowercase()}%"
                    (1..4).forEach { countStmt.setString(it, pattern) }
                }
                countStmt.executeQuery().use { rs -> if (rs.next()) rs.getLong(1) else 0L }
            }

        val totalPages = ((totalElements + size.toLong() - 1) / size).toInt()
        val offset = page * size

        val dataSql = """SELECT * FROM "user" $whereClause ORDER BY id DESC LIMIT ? OFFSET ?"""
        val content =
            connection.prepareStatement(dataSql).use { dataStmt ->
                if (search != null) {
                    val pattern = "%${search.lowercase()}%"
                    (1..4).forEach { dataStmt.setString(it, pattern) }
                    dataStmt.setInt(5, size)
                    dataStmt.setInt(6, offset)
                } else {
                    dataStmt.setInt(1, size)
                    dataStmt.setInt(2, offset)
                }
                dataStmt.executeQuery().use { rs ->
                    val list = mutableListOf<User>()
                    while (rs.next()) list.add(mapRow(rs))
                    list
                }
            }

        return Page(content, page, size, totalElements, totalPages)
    }

    private fun mapRow(rs: java.sql.ResultSet): User =
        User(
            id = UserId(rs.getString("id")),
            role = UserRole.valueOf(rs.getString("role")),
            firstname = UserFirstname(rs.getString("firstname")),
            lastname = UserLastname(rs.getString("lastname")),
            username = Username(rs.getString("username")),
            email = UserEmail(rs.getString("email")),
            passwordHash = PasswordHash(rs.getString("password_hash")),
            isActive = rs.getBoolean("is_active"),
            mustChangePassword = rs.getBoolean("must_change_password"),
        )

    companion object {
        private const val JDBC_URL = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    }
}
