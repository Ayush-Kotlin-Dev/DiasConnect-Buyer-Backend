package diasconnect.ayush.com.dao.graphql

import com.expediagroup.graphql.server.operations.Mutation
import com.expediagroup.graphql.server.operations.Query
import diasconnect.ayush.com.model.AuthResponse
import diasconnect.ayush.com.model.SignInParams
import diasconnect.ayush.com.model.SignUpParams
import diasconnect.ayush.com.model.User
import diasconnect.ayush.com.repository.auth.AuthRepository
import graphql.GraphQLException

class QueryResolver(private val userRepository: AuthRepository) : Query {
    suspend fun getUser(id: String): User? {
        return userRepository.findUserById(id)?.let { userRow ->
            User(
                id = userRow.id.toString(),
                username = userRow.username,
                email = userRow.email,
                created = userRow.createdAt,
                updated = userRow.updatedAt
            )
        }
    }
}

class MutationResolver(private val userRepository: AuthRepository) : Mutation {
    suspend fun signUp(name: String, email: String, password: String): AuthResponse {
        return try {
            userRepository.signUp(SignUpParams(name, email, password))
        } catch (e: Exception) {
            throw GraphQLException(e.message ?: "An error occurred during sign up")
        }
    }

    suspend fun signIn(email: String, password: String): AuthResponse {
        return try {
            userRepository.signIn(SignInParams(email, password))
        } catch (e: Exception) {
            throw GraphQLException(e.message ?: "An error occurred during sign in")
        }
    }
}