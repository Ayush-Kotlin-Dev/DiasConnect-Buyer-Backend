package diasconnect.buyer.com.graphql

import com.expediagroup.graphql.server.operations.Query
import com.expediagroup.graphql.server.operations.Mutation
import diasconnect.buyer.com.dao.order.OrderWithItems
import diasconnect.buyer.com.model.CreateOrderInput
import diasconnect.buyer.com.model.OrderItemType
import diasconnect.buyer.com.model.OrderType
import diasconnect.buyer.com.repository.order.OrderRepository

class OrderQuery(private val orderRepository: OrderRepository) : Query {
    suspend fun getUserOrders(userId: Long): List<OrderType> {
        return orderRepository.getUserOrders(userId).map { it.toOrderType() }
    }

    suspend fun getOrderDetails(orderId: Long): OrderType? {
        return orderRepository.getOrderDetails(orderId)?.toOrderType()
    }
}

class OrderMutation(private val orderRepository: OrderRepository) : Mutation {
    suspend fun createOrder(userId: Long, input: CreateOrderInput): OrderType {
        val orderWithItems = orderRepository.createOrder(
            userId = userId,
            items = input.items,
            total = input.total.toFloat(),
            shippingAddress = input.shippingAddress,
            paymentMethod = input.paymentMethod
        )
        return orderWithItems.toOrderType()
    }
}

fun diasconnect.buyer.com.dao.order.OrderRow.toOrderType(items: List<OrderItemType> = emptyList()): OrderType {
    return OrderType(
        id = id,
        userId = userId,
        status = status,
        total = total,
        currency = currency,
        shippingAddress = shippingAddress,
        paymentMethod = paymentMethod,
        createdAt = createdAt,
        updatedAt = updatedAt,
        items = items
    )
}

fun OrderWithItems.toOrderType(): OrderType {
    return OrderType(
        id = order.id,
        userId = order.userId,
        status = order.status,
        total = order.total,
        currency = order.currency,
        shippingAddress = order.shippingAddress,
        paymentMethod = order.paymentMethod,
        createdAt = order.createdAt,
        updatedAt = order.updatedAt,
        items = items.map { it.toOrderItemType() }
    )
}

fun diasconnect.buyer.com.dao.order.OrderItemRow.toOrderItemType(): OrderItemType {
    return OrderItemType(
        id = id,
        productId = productId,
        quantity = quantity,
        price = price.toString()
    )
}
