package diasconnect.buyer.com.util

import org.jetbrains.exposed.sql.Expression
import org.jetbrains.exposed.sql.QueryBuilder
import java.time.LocalDateTime
import java.time.ZoneId

class CurrentDateTime : Expression<LocalDateTime>() {
    override fun toQueryBuilder(queryBuilder: QueryBuilder) =
        queryBuilder { append("CURRENT_TIMESTAMP") }

    companion object {
        fun now(): LocalDateTime {
            return LocalDateTime.now(ZoneId.of("Asia/Kolkata"))
        }
    }
}