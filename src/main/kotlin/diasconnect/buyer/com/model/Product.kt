package diasconnect.buyer.com.model

data class Product(
    val id: String,
    val name: String,
    val price: Float,
    val description: String,
    val stock: Int,
    val images: List<String>,
    val categoryId: String,
    val sellerId: String,
    val createdAt: String,
    val updatedAt: String
)