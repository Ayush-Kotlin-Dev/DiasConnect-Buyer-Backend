package diasconnect.buyer.com.model

import diasconnect.buyer.com.dao.cart.CartStatus

data class Cart(
    val id: String,
    val userId: String,
    val status: CartStatus,
    val total: String,
    val currency: String,
    val createdAt: String  ,
    val updatedAt: String,
    val expiresAt: String,
    val items: List<CartItem>
)



data class CartItem(
    val id: String,
    val cartId: String,
    val productId: String,
    val quantity: Int,
    val price: String,
    val createdAt: String,
    val updatedAt: String
)