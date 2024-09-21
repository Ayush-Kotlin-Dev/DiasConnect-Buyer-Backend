package diasconnect.ayush.com.util

import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import diasconnect.ayush.com.dao.graphql.QueryResolver
import diasconnect.ayush.com.dao.graphql.UserRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.GraphqlConfig(){
    val userRepository by inject<UserRepository>()
    install(GraphQL) {
        schema {
            packages = listOf("diasconnect.ayush")
            queries = listOf(
                QueryResolver(userRepository)
            )
        }
    }
    routing {
        graphQLPostRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}