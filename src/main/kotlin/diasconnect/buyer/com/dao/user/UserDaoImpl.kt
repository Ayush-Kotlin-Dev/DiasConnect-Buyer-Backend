package diasconnect.buyer.com.dao.user

import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.security.hashPassword
import diasconnect.buyer.com.util.IdGenerator
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select

class UserDaoImpl : UserDao {
    override suspend fun createUser(username: String, email: String, passwordHash: String): UserRow? {

        return dbQuery {
            val insertStatement = UserTable.insert {
                it[id] = IdGenerator.generateId()
                it[UserTable.username] = username
                it[UserTable.email] = email
                it[UserTable.passwordHash] = hashPassword(passwordHash)
                it[UserTable.createdAt] = java.time.LocalDateTime.now()
                it[UserTable.updatedAt] = java.time.LocalDateTime.now()
            }

            insertStatement.resultedValues?.singleOrNull()?.let {
                rowToUser(it)
            }
        }

    }

    override suspend fun findUserById(id: Long): UserRow? {
        return dbQuery {
            UserTable.select { UserTable.id eq id }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun findUserByUsername(username: String): UserRow? {
        return dbQuery {
            UserTable.select { UserTable.username eq username }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun findUserByEmail(email: String): UserRow? {
        return dbQuery {
            UserTable.select { UserTable.email eq email }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    override suspend fun deleteUser(id: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(id: Int, username: String, email: String, passwordHash: String): Int {
        TODO("Not yet implemented")
    }

    private fun rowToUser(row: org.jetbrains.exposed.sql.ResultRow): UserRow {
        return UserRow(
            id = row[UserTable.id],
            username = row[UserTable.username],
            email = row[UserTable.email],
            passwordHash = row[UserTable.passwordHash],
            createdAt = row[UserTable.createdAt].toString(),
            updatedAt = row[UserTable.updatedAt].toString()
        )
    }



}