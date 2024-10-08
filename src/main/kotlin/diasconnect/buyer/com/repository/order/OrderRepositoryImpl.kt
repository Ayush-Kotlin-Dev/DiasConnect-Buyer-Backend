package diasconnect.buyer.com.repository.order

import diasconnect.buyer.com.dao.order.OrderDAO
import diasconnect.buyer.com.dao.order.OrderItemInput
import diasconnect.buyer.com.dao.order.OrderRow
import diasconnect.buyer.com.dao.order.OrderWithItems

class OrderRepositoryImpl(
    private val orderDAO: OrderDAO
) : OrderRepository {
    override suspend fun createOrder(userId: Long, items: List<OrderItemInput>, total: Float, shippingAddress: String, paymentMethod: String): OrderWithItems {
        return orderDAO.createOrder(userId, items, total, shippingAddress, paymentMethod)
    }

    override suspend fun getUserOrders(userId: Long): List<OrderWithItems> {
        return orderDAO.getOrdersByUserId(userId)
    }

    override suspend fun getOrderDetails(orderId: Long): OrderWithItems? {
        return orderDAO.getOrderWithItems(orderId)
    }
}