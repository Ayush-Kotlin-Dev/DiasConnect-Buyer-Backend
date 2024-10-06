package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem
import java.math.BigDecimal

interface CartDao {
    suspend fun createOrGetCart(userId: Long): Long
    suspend fun getActiveCartByUserId(userId: Long): Cart?
    suspend fun getCartById(cartId: Long): Cart?
    suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int, price: Float): Long
    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean
    suspend fun removeCartItem(cartItemId: Long): Boolean
    suspend fun getCartItems(cartId: Long): List<CartItem>
    suspend fun clearCart(cartId: Long): Boolean
    suspend fun updateCartStatus(cartId: Long, status: CartStatus): Boolean
    suspend fun getCartItemById(cartItemId: Long): CartItem?
}