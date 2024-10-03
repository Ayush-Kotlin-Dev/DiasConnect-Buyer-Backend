package diasconnect.buyer.com.graphql


import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import diasconnect.buyer.com.dao.cart.CartDao
import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem
import diasconnect.buyer.com.repository.cart.CartRepository

class CartQuery(
    private val cartRepository: CartRepository
) : Query {
    suspend fun getCart(userId: String): Cart? {
        return cartRepository.getCart(userId.toLong())
    }

    suspend fun getCartTotal(cartId: String): Double {
        return cartRepository.getCartTotal(cartId.toLong())
    }
}

class CartMutation(
    private val cartRepository: CartRepository
) : Mutation {
    suspend fun createCart(userId: String): Long {
        return cartRepository.createCart(userId.toLong())
    }

    suspend fun addItemToCart(cartId: String, productId: String, quantity: Int): Long {
        return cartRepository.addItemToCart(cartId.toLong(), productId.toLong(), quantity)
    }

    suspend fun updateCartItemQuantity(cartItemId: String, quantity: Int): Boolean {
        return cartRepository.updateCartItemQuantity(cartItemId.toLong(), quantity)
    }

    suspend fun removeCartItem(cartItemId: String): Boolean {
        return cartRepository.removeCartItem(cartItemId.toLong())
    }

    suspend fun clearCart(cartId: String): Boolean {
        return cartRepository.clearCart(cartId.toLong())
    }
}