package diasconnect.ayush.com.dao.graphql

import com.expediagroup.graphql.server.operations.Query
import graphql.schema.DataFetchingEnvironment

class QueryResolver(private val userRepository: UserRepository) : Query {
    fun getUser(name: String, env: DataFetchingEnvironment): User? {
        val requestedFields = env.fields.map { it.name } // Get the requested fields
        println("Requested fields: $requestedFields")
        return userRepository.getUserById(name)
    }
}
