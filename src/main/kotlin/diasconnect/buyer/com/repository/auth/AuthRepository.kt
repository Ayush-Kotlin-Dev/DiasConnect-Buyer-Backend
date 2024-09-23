package diasconnect.buyer.com.repository.auth

import diasconnect.buyer.com.dao.user.UserRow
import diasconnect.buyer.com.model.AuthResponse
import diasconnect.buyer.com.model.SignInParams
import diasconnect.buyer.com.model.SignUpParams


interface AuthRepository {
    suspend fun signUp(params: SignUpParams): AuthResponse
    suspend fun signIn(params: SignInParams): AuthResponse

    suspend fun findUserById(id: String): UserRow?
}