package diasconnect.buyer.com.graphql


import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import diasconnect.buyer.com.dao.cart.CartDao
import diasconnect.buyer.com.dao.cart.CartStatus
import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem
import diasconnect.buyer.com.repository.cart.CartRepository
import org.slf4j.LoggerFactory

class CartQuery(private val cartRepository: CartRepository) : Query {

    @GraphQLDescription("Get the active cart for a user by user ID")
    suspend fun getActiveCartByUserId(userId: Long): Cart? {
        return cartRepository.getActiveCartByUserId(userId)
    }

    @GraphQLDescription("Get a cart by its ID")
    suspend fun getCartById( cartId: Long): Cart? {
        return cartRepository.getCartById(cartId)
    }
}

class CartMutation(private val cartRepository: CartRepository) : Mutation {

    private val logger = LoggerFactory.getLogger(CartMutation::class.java)

    @GraphQLDescription("Create a new cart or return existing active cart ID for a user")
    suspend fun createOrGetCart( userId: String): Long {
        logger.info("Attempting to create or get cart for user: $userId")
        try {
            val cartId = cartRepository.createCart(userId.toLong())
            logger.info("Successfully created/retrieved cart ID: $cartId for user: $userId")
            return cartId
        } catch (e: Exception) {
            logger.error("Error creating/getting cart for user: $userId", e)
            throw e
        }
    }
    @GraphQLDescription("Add an item to a cart")
    suspend fun addItemToCart(
        cartId: String,
        productId: String,
        quantity: Int,
        price: String
    ): Long {
        return cartRepository.addItemToCart(cartId.toLong(), productId.toLong(), quantity, price.toBigDecimal())
    }

    @GraphQLDescription("Update the quantity of a cart item")
    suspend fun updateCartItemQuantity(
         cartItemId: Long,
        quantity: Int
    ): Boolean {
        return cartRepository.updateCartItemQuantity(cartItemId, quantity)
    }

    @GraphQLDescription("Remove an item from a cart")
    suspend fun removeCartItem( cartItemId: Long): Boolean {
        return cartRepository.removeCartItem(cartItemId)
    }

    @GraphQLDescription("Clear all items from a cart")
    suspend fun clearCart( cartId: Long): Boolean {
        return cartRepository.clearCart(cartId)
    }

    @GraphQLDescription("Update the status of a cart")
    suspend fun updateCartStatus(
         cartId: Long,
        status: CartStatus
    ): Boolean {
        return cartRepository.updateCartStatus(cartId, status)
    }
}