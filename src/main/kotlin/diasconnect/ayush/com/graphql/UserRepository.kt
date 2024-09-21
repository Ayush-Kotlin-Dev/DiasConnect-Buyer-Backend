package diasconnect.ayush.com.dao.graphql

import kotlinx.serialization.Serializable

class UserRepository(private val userDao: UserDaoGraph) {
    fun getUserById(name: String): User? {
        // Business logic, data transformations can happen here
        return userDao.fetchUserById(name)
    }
}





@Serializable
data class User(
    val id: String,
    val name: String,
    val bio: String,
    val email: String = "",
    val imageUrl: String? = null,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val isFollowing: Boolean,
    val isOwnProfile: Boolean,
    val token : String? = null
)