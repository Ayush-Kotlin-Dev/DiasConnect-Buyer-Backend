package diasconnect.buyer.com.dao.order


interface OrderDAO {
    suspend fun createOrder(userId: Long, items: List<OrderItemInput>, total: Float, shippingAddress: String, paymentMethod: String): OrderWithItems
    suspend fun getOrdersByUserId(userId: Long): List<OrderWithItems>
    suspend fun getOrderWithItems(orderId: Long): OrderWithItems?
}