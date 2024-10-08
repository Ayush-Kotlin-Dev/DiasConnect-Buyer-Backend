package diasconnect.buyer.com.graphql.scalars

import graphql.language.FloatValue
import graphql.language.StringValue
import graphql.schema.*

object FloatScalar : Coercing<Float, String> {
    override fun serialize(dataFetcherResult: Any): String = when (dataFetcherResult) {
        is Float -> dataFetcherResult.toString()
        is Double -> dataFetcherResult.toFloat().toString()
        else -> throw CoercingSerializeException("Expected a Float or Double")
    }

    override fun parseValue(input: Any): Float = when (input) {
        is Float -> input
        is Double -> input.toFloat()
        is String -> input.toFloat()
        else -> throw CoercingParseValueException("Expected a Float, Double, or String")
    }

    override fun parseLiteral(input: Any): Float = when (input) {
        is FloatValue -> input.value.toFloat()
        is StringValue -> input.value.toFloat()
        else -> throw CoercingParseLiteralException("Expected a FloatValue or StringValue")
    }
}

val GraphQLFloat = GraphQLScalarType.newScalar()
    .name("Float")
    .description("A custom scalar that handles float values as strings to preserve precision")
    .coercing(FloatScalar)
    .build()