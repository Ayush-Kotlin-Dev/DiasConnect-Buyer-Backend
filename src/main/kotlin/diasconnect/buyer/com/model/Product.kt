package diasconnect.buyer.com.model

data class Product(
    val id: Long,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val images: List<String>,
    val categoryId: Long,
    val sellerId: Long,
    val createdAt: String,
    val updatedAt: String
)