package diasconnect.ayush.com.dao.user

import diasconnect.ayush.com.util.CurrentDateTime
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object UserTable: Table(name = "users"){
    val id = long(name = "user_id").autoIncrement()
    val username = varchar(name = "username", length = 250)
    val email = varchar(name = "email", length = 250)
    val passwordHash = varchar(name = "password_hash", length = 100)
    val createdAt = datetime(name = "created_at").defaultExpression(defaultValue = CurrentDateTime())
    val updatedAt = datetime(name = "updated_at").defaultExpression(defaultValue = CurrentDateTime())
    override val primaryKey: PrimaryKey
        get() = PrimaryKey(id)
}

@Serializable
data class UserRow(
    val id: Long,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAt: String,
    val updatedAt: String
)













