package diasconnect.buyer.com.graphql

import com.expediagroup.graphql.server.operations.Query
import diasconnect.buyer.com.model.Product
import diasconnect.buyer.com.repository.product.BuyerProductRepository

class ProductQuery(
    private val productRepository: BuyerProductRepository
) : Query {
    suspend fun products(): List<Product> = productRepository.getAllProducts()

    suspend fun product(id: String): Product? = productRepository.getProductById(id.toLong())

    suspend fun productsByCategory(categoryId: String): List<Product> =
        productRepository.getProductsByCategory(categoryId.toLong())

    suspend fun searchProducts(query: String): List<Product> =
        productRepository.searchProducts(query)
}