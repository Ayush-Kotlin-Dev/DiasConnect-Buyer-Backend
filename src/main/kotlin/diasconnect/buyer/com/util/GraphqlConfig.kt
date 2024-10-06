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
import diasconnect.buyer.com.graphql.CartMutation
import diasconnect.buyer.com.graphql.CartQuery
import diasconnect.buyer.com.graphql.ProductQuery
import diasconnect.buyer.com.repository.auth.AuthRepository
import diasconnect.buyer.com.repository.cart.CartRepository
import diasconnect.buyer.com.repository.product.BuyerProductRepository
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import com.expediagroup.graphql.generator.hooks.SchemaGeneratorHooks
import diasconnect.buyer.com.graphql.scalars.GraphQLLong
import diasconnect.buyer.com.graphql.scalars.LongScalar
import graphql.schema.GraphQLType
import kotlin.reflect.KType



class CustomSchemaGeneratorHooks : SchemaGeneratorHooks {
    override fun willGenerateGraphQLType(type: KType): GraphQLType? {
        return when (type.classifier) {
            Long::class -> GraphQLLong
            else -> null
        }
    }
}

fun Application.GraphqlConfig() {
    val userRepository by inject<AuthRepository>()
    val productRepository by inject<BuyerProductRepository>()
    val cartRepository by inject<CartRepository>()

    install(GraphQL) {
        schema {
            packages = listOf("diasconnect.buyer.com")
            queries = listOf(
                QueryResolver(userRepository),
                ProductQuery(productRepository),
                CartQuery(cartRepository)
            )
            mutations = listOf(
                MutationResolver(userRepository),
                CartMutation(cartRepository)
            )
            hooks = CustomSchemaGeneratorHooks()
        }
    }

    val config = SchemaGeneratorConfig(
        supportedPackages = listOf("diasconnect.buyer.com"),
        hooks = CustomSchemaGeneratorHooks()
    )

    toSchema(
        config = config,
        queries = listOf(
            TopLevelObject(QueryResolver(userRepository)),
            TopLevelObject(ProductQuery(productRepository))
        ),
        mutations = listOf(TopLevelObject(MutationResolver(userRepository)))
    )

    routing {
        graphQLPostRoute()
        graphiQLRoute()
        graphQLSDLRoute()
    }
}