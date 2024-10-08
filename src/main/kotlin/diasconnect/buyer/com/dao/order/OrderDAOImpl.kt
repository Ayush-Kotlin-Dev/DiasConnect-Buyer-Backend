package diasconnect.buyer.com.dao.order

import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.dao.product.ProductsTable
import diasconnect.buyer.com.util.IdGenerator
import org.jetbrains.exposed.sql.*

class OrderDAOImpl : OrderDAO {
    override suspend fun createOrder(userId: Long, items: List<OrderItemInput>, total: Float, shippingAddress: String, paymentMethod: String): OrderWithItems = dbQuery {

        val orderId = OrdersTable.insert {
            it[OrdersTable.id] = IdGenerator.generateId()
            it[OrdersTable.userId] = userId
            val sellerId = ProductsTable.slice(ProductsTable.sellerId)
                .select { ProductsTable.id eq items.first().productId }
                .singleOrNull()?.get(ProductsTable.sellerId)
                ?: throw IllegalStateException("Seller not found for product ${items.first().productId}") //TODO handle this better by just taking the seller id fromm client side
            it[OrdersTable.sellerId] = sellerId
            it[OrdersTable.status] = OrderStatus.PENDING
            it[OrdersTable.total] = total
            it[OrdersTable.shippingAddress] = shippingAddress
            it[OrdersTable.paymentMethod] = paymentMethod
        } get OrdersTable.id

        val orderItems = items.map { item ->
            OrderItemsTable.insert {
                it[OrderItemsTable.id] = IdGenerator.generateId()
                it[OrderItemsTable.orderId] = orderId
                it[OrderItemsTable.productId] = item.productId
                it[OrderItemsTable.quantity] = item.quantity
                it[OrderItemsTable.price] = item.price.toFloat()
            }
        }.map { it[OrderItemsTable.id] }

        val order = OrdersTable.select { OrdersTable.id eq orderId }.map { toOrderRow(it) }.single()
        val orderItemRows = OrderItemsTable.select { OrderItemsTable.id inList orderItems }.map { toOrderItemRow(it) }

        OrderWithItems(order, orderItemRows)
    }

    override suspend fun getOrdersByUserId(userId: Long): List<OrderWithItems> = dbQuery {
        // First, fetch all orders for the user
        val orders = OrdersTable
            .select { OrdersTable.userId eq userId }
            .orderBy(OrdersTable.createdAt to SortOrder.DESC)
            .map { toOrderRow(it) }

        val orderIds = orders.map { it.id }
        val itemsByOrderId = OrderItemsTable
            .select { OrderItemsTable.orderId inList orderIds }
            .map { toOrderItemRow(it) }
            .groupBy { it.orderId }

        orders.map { order ->
            OrderWithItems(order, itemsByOrderId[order.id] ?: emptyList())
        }
    }

    override suspend fun getOrderWithItems(orderId: Long): OrderWithItems? = dbQuery {
        val order = OrdersTable.select { OrdersTable.id eq orderId }
            .map { toOrderRow(it) }
            .singleOrNull() ?: return@dbQuery null

        val items = OrderItemsTable.select { OrderItemsTable.orderId eq orderId }
            .map { toOrderItemRow(it) }

        OrderWithItems(order, items)
    }

    private fun toOrderRow(row: ResultRow): OrderRow =
        OrderRow(
            id = row[OrdersTable.id],
            userId = row[OrdersTable.userId],
            status = row[OrdersTable.status],
            total = row[OrdersTable.total],
            currency = row[OrdersTable.currency],
            shippingAddress = row[OrdersTable.shippingAddress],
            paymentMethod = row[OrdersTable.paymentMethod],
            createdAt = row[OrdersTable.createdAt].toString(),
            updatedAt = row[OrdersTable.updatedAt].toString()
        )

    private fun toOrderItemRow(row: ResultRow): OrderItemRow =
        OrderItemRow(
            id = row[OrderItemsTable.id],
            orderId = row[OrderItemsTable.orderId],
            productId = row[OrderItemsTable.productId],
            quantity = row[OrderItemsTable.quantity],
            price = row[OrderItemsTable.price],
            createdAt = row[OrderItemsTable.createdAt].toString(),
            updatedAt = row[OrderItemsTable.updatedAt].toString()
        )
}


data class OrderItemInput(
    val productId: Long,
    val quantity: Int,
    val price: String
)

data class OrderWithItems(
    val order: OrderRow,
    val items: List<OrderItemRow>
)