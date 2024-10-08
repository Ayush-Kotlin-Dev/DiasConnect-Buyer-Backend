package diasconnect.buyer.com.graphql.scalars

import graphql.language.IntValue
import graphql.language.StringValue
import graphql.schema.*

object LongScalar : Coercing<Long, String> {
    override fun serialize(dataFetcherResult: Any): String = when (dataFetcherResult) {
        is Long -> dataFetcherResult.toString()
        is Int -> dataFetcherResult.toLong().toString()
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
        is StringValue -> input.value.toLong()
        else -> throw CoercingParseLiteralException("Expected an IntValue or StringValue")
    }
}

val GraphQLLong = GraphQLScalarType.newScalar()
    .name("Long")
    .description("A custom scalar that handles long integers as strings to preserve precision")
    .coercing(LongScalar)
    .build()