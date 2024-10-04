package diasconnect.buyer.com.model

import diasconnect.buyer.com.dao.cart.CartStatus

data class Cart(
    val id: Long,
    val userId: Long,
    val status: CartStatus,
    val total: String,
    val currency: String,
    val createdAt: String  ,
    val updatedAt: String,
    val expiresAt: String,
    val items: List<CartItem>
)



data class CartItem(
    val id: Long,
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
    val price: String,
    val createdAt: String,
    val updatedAt: String
)