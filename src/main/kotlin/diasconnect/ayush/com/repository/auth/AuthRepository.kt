package diasconnect.ayush.com.repository.auth

import diasconnect.ayush.com.dao.user.UserRow
import diasconnect.ayush.com.model.AuthResponse
import diasconnect.ayush.com.model.SignInParams
import diasconnect.ayush.com.model.SignUpParams
import diasconnect.ayush.com.model.User
import diasconnect.ayush.com.util.Response


interface AuthRepository {
    suspend fun signUp(params: SignUpParams): AuthResponse
    suspend fun signIn(params: SignInParams): AuthResponse

    suspend fun findUserById(id: String): UserRow?
}