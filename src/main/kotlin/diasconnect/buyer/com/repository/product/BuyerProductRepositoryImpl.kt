package diasconnect.buyer.com.repository.product

import diasconnect.buyer.com.dao.product.BuyerProductDao
import diasconnect.buyer.com.dao.product.ProductRow
import diasconnect.buyer.com.model.Product

class BuyerProductRepositoryImpl(
    private val dao: BuyerProductDao
) : BuyerProductRepository {
    override suspend fun getAllProducts(): List<Product> = dao.getAllProducts().map { it.toProduct() }
    override suspend fun getProductById(id: Long): Product? = dao.getProductById(id)?.toProduct()
    override suspend fun getProductsByCategory(categoryId: Long): List<Product> =
        dao.getProductsByCategory(categoryId).map { it.toProduct() }

    override suspend fun searchProducts(query: String): List<Product> = dao.searchProducts(query).map { it.toProduct() }

    private fun ProductRow.toProduct(): Product = Product(
        id = id,
        name = name,
        price = price,
        description = description,
        stock = stock,
        images = images,
        categoryId = categoryId,
        sellerId = sellerId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
