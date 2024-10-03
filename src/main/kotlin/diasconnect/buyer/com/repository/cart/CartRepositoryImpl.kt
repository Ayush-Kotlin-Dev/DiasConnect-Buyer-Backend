package diasconnect.buyer.com.repository.cart

import diasconnect.buyer.com.dao.cart.CartDao
import diasconnect.buyer.com.dao.product.BuyerProductDao
import diasconnect.buyer.com.model.Cart
import diasconnect.buyer.com.model.CartItem

class CartRepositoryImpl(
    private val cartDao: CartDao,
    private val productDao: BuyerProductDao
) : CartRepository {

    override suspend fun createCart(userId: Long): Long {
        return cartDao.createCart(userId)
    }

    override suspend fun getCart(userId: Long): Cart?  {
        val cartRow = cartDao.getCart(userId)
        return  cartRow?.let {
            val cartItems = getCartItems(it.id)
            Cart(id = it.id, userId = it.userId, items = cartItems)
        }

    }

    override suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int): Long  {
        return cartDao.addItemToCart(cartId, productId, quantity)
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean  {
        return cartDao.updateCartItemQuantity(cartItemId, quantity)
    }

    override suspend fun removeCartItem(cartItemId: Long): Boolean  {
        return  cartDao.removeCartItem(cartItemId)
    }

    override suspend fun getCartItems(cartId: Long): List<CartItem>  {
        return cartDao.getCartItems(cartId).map { item ->
            CartItem(id = item.id, productId = item.productId, quantity = item.quantity)
        }
    }

    override suspend fun clearCart(cartId: Long): Boolean  {
        return cartDao.clearCart(cartId)
    }

    override suspend fun getCartTotal(cartId: Long): Double  {
        val cartItems = cartDao.getCartItems(cartId)
        return cartItems.sumOf { item ->
            val product = productDao.getProductById(item.productId)
            (product?.price?.toDouble() ?: 0.0) * item.quantity
        }
    }
}