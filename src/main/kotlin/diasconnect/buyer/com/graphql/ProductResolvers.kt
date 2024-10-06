package diasconnect.buyer.com.graphql

import com.expediagroup.graphql.server.operations.Query
import diasconnect.buyer.com.model.Product
import diasconnect.buyer.com.repository.product.BuyerProductRepository

class ProductQuery(
    private val productRepository: BuyerProductRepository
) : Query {
    suspend fun products(): List<Product> = productRepository.getAllProducts()

    suspend fun product(id: Long): Product? = productRepository.getProductById(id)

    suspend fun productsByCategory(categoryId: Long): List<Product> =
        productRepository.getProductsByCategory(categoryId)

    suspend fun searchProducts(query: String): List<Product> =
        productRepository.searchProducts(query)
}