package diasconnect.buyer.com.model

import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import diasconnect.buyer.com.dao.order.OrderItemInput
import diasconnect.buyer.com.dao.order.OrderStatus

@GraphQLDescription("Order details")
data class OrderType(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val total: Float,
    val currency: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<OrderItemType>
)

@GraphQLDescription("Order item details")
data class OrderItemType(
    val id: Long,
    val productId: Long,
    val quantity: Int,
    val price: String
)


@GraphQLDescription("Input for creating an order")
data class CreateOrderInput(
    val items: List<OrderItemInput>,
    val total: String,
    val shippingAddress: String,
    val paymentMethod: String
)