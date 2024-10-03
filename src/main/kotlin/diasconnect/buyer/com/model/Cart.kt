package diasconnect.buyer.com.model


data class Cart(
    val id: Long,
    val userId: Long,
    val items: List<CartItem>
)

data class CartItem(
    val id: Long,
    val productId: Long,
    val quantity: Int
)