package diasconnect.buyer.com.dao.order

import diasconnect.buyer.com.util.CurrentDateTime
import diasconnect.buyer.com.dao.user.UserTable
import diasconnect.buyer.com.dao.product.ProductsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object OrdersTable : Table("orders") {
    val id = long("order_id").autoIncrement()
    val userId = long("user_id").references(UserTable.id)
    val sellerId = long("seller_id").references(UserTable.id)
    val status = enumerationByName("status", 20, OrderStatus::class)
    val total = float("total")
    val currency = varchar("currency", 3).default("USD")
    val shippingAddress = text("shipping_address")
    val paymentMethod = varchar("payment_method", 50)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)

}
enum class OrderStatus {
    PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED
}
object OrderItemsTable : Table("order_items") {
    val id = long("item_id").autoIncrement()
    val orderId = long("order_id").references(OrdersTable.id)
    val productId = long("product_id").references(ProductsTable.id)
    val quantity = integer("quantity")
    val price = float("price")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)
}

data class OrderRow(
    val id: Long,
    val userId: Long,
    val status: OrderStatus,
    val total: Float,
    val currency: String,
    val shippingAddress: String,
    val paymentMethod: String,
    val createdAt: String,
    val updatedAt: String
)

data class OrderItemRow(
    val id: Long,
    val orderId: Long,
    val productId: Long,
    val quantity: Int,
    val price: Float,
    val createdAt: String,
    val updatedAt: String
)