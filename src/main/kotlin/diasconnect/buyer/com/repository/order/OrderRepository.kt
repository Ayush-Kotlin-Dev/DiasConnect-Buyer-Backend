package diasconnect.buyer.com.repository.order

import diasconnect.buyer.com.dao.order.*

interface OrderRepository {

    suspend fun createOrder(userId: Long, items: List<OrderItemInput>, total: Float, shippingAddress: String, paymentMethod: String): OrderWithItems

    suspend fun getUserOrders(userId: Long): List<OrderWithItems>

    suspend fun getOrderDetails(orderId: Long): OrderWithItems?

}