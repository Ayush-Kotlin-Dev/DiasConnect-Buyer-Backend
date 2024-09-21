package diasconnect.ayush.com.repository.auth

import diasconnect.ayush.com.dao.user.UserDao
import diasconnect.ayush.com.dao.user.UserRow
import diasconnect.ayush.com.model.AuthResponse
import diasconnect.ayush.com.model.AuthResponseData
import diasconnect.ayush.com.model.SignInParams
import diasconnect.ayush.com.model.SignUpParams
import diasconnect.ayush.com.plugins.generateToken
import diasconnect.ayush.com.security.hashPassword
import io.ktor.http.*

class AuthRepositoryImpl(
    private val userDao: UserDao
) : AuthRepository {
    override suspend fun signUp(params: SignUpParams): AuthResponse {
        if (userAlreadyExists(params.email)) {
            throw IllegalArgumentException("A user with this email already exists")
        }

        val createdUser = userDao.createUser(params.name, params.email, params.password)
            ?: throw IllegalStateException("Failed to create user")

        val token = generateToken(createdUser.email)

        return AuthResponse(
            data = AuthResponseData(
                id = createdUser.id.toString(),
                name = createdUser.username,
                token = token,
                created = createdUser.createdAt,
                updated = createdUser.updatedAt,
                email = createdUser.email
            )
        )
    }

    override suspend fun signIn(params: SignInParams): AuthResponse {
        val user = userDao.findUserByEmail(params.email)
            ?: throw IllegalArgumentException("No user found with this email")

        if (!verifyPassword(params.password, user.passwordHash)) {
            throw IllegalArgumentException("Invalid password")
        }

        val token = generateToken(user.email)

        return AuthResponse(
            data = AuthResponseData(
                id = user.id.toString(),
                name = user.username,
                token = token,
                created = user.createdAt,
                updated = user.updatedAt,
                email = user.email
            )
        )
    }

    override suspend fun findUserById(id: String): UserRow? {
        return userDao.findUserById(id.toLong())
    }

    private suspend fun userAlreadyExists(email: String): Boolean {
        return userDao.findUserByEmail(email) != null
    }

    private fun verifyPassword(inputPassword: String, storedHash: String): Boolean {
        // Implement password verification logic here
        // This should compare the hash of the input password with the stored hash
        return hashPassword(inputPassword) == storedHash
    }
}