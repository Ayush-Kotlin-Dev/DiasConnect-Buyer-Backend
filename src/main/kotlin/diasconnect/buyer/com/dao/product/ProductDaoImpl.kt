package diasconnect.buyer.com.dao.product

import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.model.Product
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.or
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class BuyerProductDaoImpl : BuyerProductDao {
    override suspend fun getAllProducts(): List<ProductRow> = dbQuery {
        (ProductsTable leftJoin ProductImagesTable)
            .slice(ProductsTable.columns + ProductImagesTable.imageUrl)
            .selectAll()
            .groupBy { it[ProductsTable.id] }
            .map { (_, rows) ->
                val productRow = rows.first()
                val images = rows.map { it[ProductImagesTable.imageUrl] }
                rowToProduct(productRow, images)
            }
    }

    override suspend fun getProductById(id: Long): ProductRow? = dbQuery {
        (ProductsTable leftJoin ProductImagesTable)
            .slice(ProductsTable.columns + ProductImagesTable.imageUrl)
            .select { ProductsTable.id eq id }
            .groupBy { it[ProductsTable.id] }
            .map { (_, rows) ->
                val productRow = rows.first()
                val images = rows.map { it[ProductImagesTable.imageUrl] }
                rowToProduct(productRow, images)
            }
            .singleOrNull()
    }

    override suspend fun getProductsByCategory(categoryId: Long): List<ProductRow> = dbQuery {
        (ProductsTable leftJoin ProductImagesTable)
            .slice(ProductsTable.columns + ProductImagesTable.imageUrl)
            .select { ProductsTable.categoryId eq categoryId }
            .groupBy { it[ProductsTable.id] }
            .map { (_, rows) ->
                val productRow = rows.first()
                val images = rows.map { it[ProductImagesTable.imageUrl] }
                rowToProduct(productRow, images)
            }
    }

    override suspend fun searchProducts(query: String): List<ProductRow> = dbQuery {
        (ProductsTable leftJoin ProductImagesTable)
            .slice(ProductsTable.columns + ProductImagesTable.imageUrl)
            .select { ProductsTable.name like "%$query%" or (ProductsTable.description like "%$query%") }
            .groupBy { it[ProductsTable.id] }
            .map { (_, rows) ->
                val productRow = rows.first()
                val images = rows.map { it[ProductImagesTable.imageUrl] }
                rowToProduct(productRow, images)
            }
    }

    private fun rowToProduct(row: ResultRow, images: List<String>): ProductRow {
        return ProductRow(
            id = row[ProductsTable.id],
            name = row[ProductsTable.name],
            price = row[ProductsTable.price],
            description = row[ProductsTable.description],
            stock = row[ProductsTable.stock],
            images = images,
            categoryId = row[ProductsTable.categoryId],
            sellerId = row[ProductsTable.sellerId],
            createdAt = row[ProductsTable.createdAt].toString(),
            updatedAt = row[ProductsTable.updatedAt].toString()
        )
    }
}