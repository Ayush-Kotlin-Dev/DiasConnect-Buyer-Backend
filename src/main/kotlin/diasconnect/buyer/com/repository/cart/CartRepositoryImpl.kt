// src/main/kotlin/diasconnect/buyer/com/repository/CartRepositoryImpl.kt
package diasconnect.buyer.com.repository.cart

import diasconnect.buyer.com.dao.cart.CartDao
import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem
import diasconnect.buyer.com.dao.cart.CartStatus
import java.math.BigDecimal

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override suspend fun createCart(userId: Long): Long {
        return cartDao.createCart(userId)
    }

    override suspend fun getActiveCartByUserId(userId: Long): Cart? {
        return cartDao.getActiveCartByUserId(userId)
    }

    override suspend fun getCartById(cartId: Long): Cart? {
        return cartDao.getCartById(cartId)
    }

    override suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int, price: Float): Long {
        return cartDao.addItemToCart(cartId, productId, quantity,price)
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean {
        return cartDao.updateCartItemQuantity(cartItemId, quantity)
    }

    override suspend fun removeCartItem(cartItemId: Long): Boolean {
        return cartDao.removeCartItem(cartItemId)
    }

    override suspend fun getCartItems(cartId: Long): List<CartItem> {
        return cartDao.getCartItems(cartId)
    }

    override suspend fun clearCart(cartId: Long): Boolean {
        return cartDao.clearCart(cartId)
    }

    override suspend fun updateCartStatus(cartId: Long, status: CartStatus): Boolean {
        return cartDao.updateCartStatus(cartId, status)
    }

    override suspend fun getCartItemById(id: Long): CartItem? {
        return cartDao.getCartItemById(id)
    }
}