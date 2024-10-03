package diasconnect.buyer.com.repository.cart

import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem

interface CartRepository {
    suspend fun createCart(userId: Long): Long
    suspend fun getCart(userId: Long): Cart?
    suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int): Long
    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean
    suspend fun removeCartItem(cartItemId: Long): Boolean
    suspend fun getCartItems(cartId: Long): List<CartItem>
    suspend fun clearCart(cartId: Long): Boolean
    suspend fun getCartTotal(cartId: Long): Double
}