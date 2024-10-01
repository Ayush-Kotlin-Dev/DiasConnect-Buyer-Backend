package diasconnect.buyer.com.repository.product

import diasconnect.buyer.com.model.Product

interface BuyerProductRepository {
    suspend fun getAllProducts(): List<Product>
    suspend fun getProductById(id: Long): Product?
    suspend fun getProductsByCategory(categoryId: Long): List<Product>
    suspend fun searchProducts(query: String): List<Product>
}