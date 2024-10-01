package diasconnect.buyer.com.dao.product

import diasconnect.buyer.com.model.Product

interface BuyerProductDao {
    suspend fun getAllProducts(): List<ProductRow>
    suspend fun getProductById(id: Long): ProductRow?
    suspend fun getProductsByCategory(categoryId: Long): List<ProductRow>
    suspend fun searchProducts(query: String): List<ProductRow>
}