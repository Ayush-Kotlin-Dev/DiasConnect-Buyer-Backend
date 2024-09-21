package diasconnect.ayush.com.dao.graphql


import diasconnect.ayush.com.graphql.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserDaoGraph {
    fun fetchUserById(name: String): User? {
        return transaction {
            UserTable.select { UserTable.name like name }
                .map { rowToUser(it) }
                .singleOrNull()
        }
    }

    private fun rowToUser(row: ResultRow): User {
        return User(
            id = row[UserTable.id].toString(),
            name = row[UserTable.name],
            bio = row[UserTable.bio],
            imageUrl = row[UserTable.imageUrl],
            followersCount = row[UserTable.followersCount],
            followingCount = row[UserTable.followingCount],
            isFollowing = false,
            isOwnProfile = false,
            email = row[UserTable.email]
        )
    }
}

