package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.util.CurrentDateTime
import diasconnect.buyer.com.dao.product.ProductsTable
import io.ktor.client.utils.EmptyContent.status
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime
import java.math.BigDecimal

object CartTable : Table("carts") {
    val id = long("id").autoIncrement()
    val userId = long("user_id")
    val status = enumerationByName("status", 20, CartStatus::class)
    val total = float("total")
    val currency = varchar("currency", 3).default("USD")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    val expiresAt = datetime("expires_at").nullable()


    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex(userId, status)
    }
}
enum class CartStatus {
    ACTIVE, ABANDONED, COMPLETED
}

object CartItemTable : Table("cart_items") {
    val id = long("id").autoIncrement()
    val cartId = long("cart_id").references(CartTable.id)
    val productId = long("product_id").references(ProductsTable.id)
    val quantity = integer("quantity")
    val price = float("price")
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)
}

data class CartRow(
    val id: Long,
    val userId: Long,
    val status: CartStatus,
    val total: Float,
    val currency: String,
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