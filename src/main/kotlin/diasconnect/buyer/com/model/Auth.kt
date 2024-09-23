package diasconnect.buyer.com.model

import kotlinx.serialization.Serializable

@Serializable
data class SignUpParams(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class SignInParams(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val data: AuthResponseData? = null,
    val errorMessage: String? = null
)

@Serializable
data class User(
    val id: String,
    val username: String,
    val email: String,
    val created: String,
    val updated: String,
)

@Serializable
data class AuthResponseData(
    val id:String,
    val name: String,
    val email: String,
    val token: String,
    val created: String,
    val updated: String,


)