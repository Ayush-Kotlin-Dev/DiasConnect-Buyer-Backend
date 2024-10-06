package diasconnect.buyer.com.repository.cart

import diasconnect.buyer.com.dao.cart.CartStatus
import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem
import kotlinx.html.B
import java.math.BigDecimal

interface CartRepository {
    suspend fun createCart(userId: Long): Long
    suspend fun getActiveCartByUserId(userId: Long): Cart?
    suspend fun getCartById(cartId: Long): Cart?
    suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int, price: Float): Long
    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean
    suspend fun removeCartItem(cartItemId: Long): Boolean
    suspend fun getCartItems(cartId: Long): List<CartItem>
    suspend fun clearCart(cartId: Long): Boolean
    suspend fun updateCartStatus(cartId: Long, status: CartStatus): Boolean
    suspend fun getCartItemById(id: Long): CartItem?
}