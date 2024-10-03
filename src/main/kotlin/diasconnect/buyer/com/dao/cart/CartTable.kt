package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.util.CurrentDateTime
import diasconnect.buyer.com.dao.product.ProductsTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object CartTable : Table("carts") {
    val id = long("cart_id").autoIncrement()
    val userId = long("user_id")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())

    override val primaryKey = PrimaryKey(id)
    init {
        uniqueIndex(userId)
    }
}
object CartItemsTable : Table("cart_items") {
    val id = long("item_id").autoIncrement()
    val cartId = long("cart_id").references(CartTable.id)
    val productId = long("product_id").references(ProductsTable.id)
    val quantity = integer("quantity")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)
}

data class CartRow(
    val id: Long,
    val userId: Long,
    val createdAt: String,
    val updatedAt: String
)

data class CartItemRow(
    val id: Long,
    val cartId: Long,
    val productId: Long,
    val quantity: Int,
    val createdAt: String,
    val updatedAt: String
)