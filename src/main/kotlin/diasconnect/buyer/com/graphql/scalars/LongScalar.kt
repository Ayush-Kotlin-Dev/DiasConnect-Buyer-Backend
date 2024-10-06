package diasconnect.buyer.com.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.Coercing
import graphql.schema.GraphQLScalarType

// Custom Long Scalar definition
val LongScalar = GraphQLScalarType.newScalar()
    .name("Long")
    .description("Long type")
    .coercing(object : Coercing<Long, Long> {
        override fun serialize(dataFetcherResult: Any): Long? = when (dataFetcherResult) {
            is Long -> dataFetcherResult
            is Int -> dataFetcherResult.toLong()
            is String -> dataFetcherResult.toLongOrNull()
            else -> null
        }

        override fun parseValue(input: Any): Long? = serialize(input)

        override fun parseLiteral(input: Any): Long? = when (input) {
            is graphql.language.IntValue -> input.value.toLong()
            is graphql.language.StringValue -> input.value.toLongOrNull()
            else -> null
        }
    })
    .build()
