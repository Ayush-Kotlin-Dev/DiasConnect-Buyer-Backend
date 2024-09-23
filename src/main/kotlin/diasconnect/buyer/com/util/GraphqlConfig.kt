package diasconnect.buyer.com.util

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import com.expediagroup.graphql.server.ktor.GraphQL
import com.expediagroup.graphql.server.ktor.graphQLPostRoute
import com.expediagroup.graphql.server.ktor.graphQLSDLRoute
import com.expediagroup.graphql.server.ktor.graphiQLRoute
import diasconnect.buyer.com.dao.graphql.MutationResolver
import diasconnect.buyer.com.dao.graphql.QueryResolver
import diasconnect.buyer.com.repository.auth.AuthRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Application.GraphqlConfig(){
    val userRepository by inject<AuthRepository>()
    install(GraphQL) {
        schema {
            packages = listOf("diasconnect.buyer.com")
            queries = listOf(
                QueryResolver(userRepository)
            )
            mutations = listOf(
                MutationResolver(userRepository)
            )

        }
    }
    val config = SchemaGeneratorConfig(supportedPackages = listOf("diasconnect.buyer.com"))
    toSchema(config = config , queries = listOf(TopLevelObject(QueryResolver(userRepository))), mutations = listOf(TopLevelObject(MutationResolver(userRepository))))
    routing {
        graphQLPostRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}