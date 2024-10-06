package diasconnect.buyer.com.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.*

object LongScalar : Coercing<Long, Long> {
    override fun serialize(dataFetcherResult: Any): Long = when (dataFetcherResult) {
        is Long -> dataFetcherResult
        is Int -> dataFetcherResult.toLong()
        else -> throw CoercingSerializeException("Expected a Long or Int")
    }

    override fun parseValue(input: Any): Long = when (input) {
        is Long -> input
        is Int -> input.toLong()
        is String -> input.toLong()
        else -> throw CoercingParseValueException("Expected a Long, Int, or String")
    }

    override fun parseLiteral(input: Any): Long = when (input) {
        is IntValue -> input.value.toLong()
        else -> throw CoercingParseLiteralException("Expected an IntValue")
    }
}

val GraphQLLong = GraphQLScalarType.newScalar()
    .name("Long")
    .description("A custom scalar that handles long integers")
    .coercing(LongScalar)
    .build()