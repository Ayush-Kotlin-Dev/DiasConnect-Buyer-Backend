package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.dao.product.ProductsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

interface CartDao {
    suspend fun createCart(userId: Long): Long

    suspend fun getCart(userId: Long): CartRow?

    suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int): Long

    suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int) : Boolean

    suspend fun removeCartItem(cartItemId: Long) : Boolean

    suspend fun getCartItems(cartId: Long): List<CartItemRow>

    abstract fun clearCart(cartId: Long): Boolean


}