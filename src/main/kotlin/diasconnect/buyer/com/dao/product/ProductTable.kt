package diasconnect.buyer.com.dao.product

import diasconnect.buyer.com.util.CurrentDateTime
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object ProductsTable : Table("products") {
    val id = long("product_id").autoIncrement()
    val name = varchar("name", 250)
    val price = float("price")
    val description = varchar("description", 1000)
    val stock = integer("stock")
    val categoryId = long("category_id")
    val sellerId = long("seller_id")
//    val sku = varchar("sku", 100).nullable() // Optional SKU
//    val discount = float("discount").nullable() // Optional discount
//    val rating = float("rating").nullable() // Optional average rating
//    val weight = float("weight").nullable() // Optional weight
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)
}

object ProductImagesTable : Table("product_images") {
    val id = long("image_id").autoIncrement()
    val productId = long("product_id").references(ProductsTable.id)
    val imageUrl = varchar("image_url", 500)
//    val altText = varchar("alt_text", 250).nullable() // Optional alt text
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime())
    override val primaryKey = PrimaryKey(id)
}

data class ProductRow(
    val id: Long,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val images: List<String> = emptyList(),
    val categoryId: Long,
    val sellerId: Long,
    val createdAt: String,
    val updatedAt: String
)
