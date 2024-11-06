package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.dao.product.ProductImagesTable
import diasconnect.buyer.com.dao.product.ProductsTable
import diasconnect.buyer.com.model.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.times
import org.jetbrains.exposed.sql.sum
import diasconnect.buyer.com.util.CurrentDateTime
import diasconnect.buyer.com.util.IdGenerator
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class CartDaoImpl : CartDao {
    private val logger = LoggerFactory.getLogger(CartDaoImpl::class.java)

    override suspend fun createOrGetCart(userId: Long): Long = dbQuery {
        logger.info("Checking for existing cart or creating new cart for user: $userId")
        val existingCartId = CartTable
            .slice(CartTable.id)
            .select { (CartTable.userId eq userId) and (CartTable.status eq CartStatus.ACTIVE) }
            .singleOrNull()?.get(CartTable.id)

        if (existingCartId != null) {
            logger.info("Existing active cart found for user: $userId. Cart ID: $existingCartId")
            return@dbQuery existingCartId
        }

        logger.info("No existing active cart found. Creating new cart for user: $userId")
        try {

            val now = CurrentDateTime()
            val cartId = CartTable.insert {
                it[CartTable.id] = IdGenerator.generateId()
                it[CartTable.userId] = userId
                it[status] = CartStatus.ACTIVE
                it[total] = 0.0f
                it[currency] = "USD"
                it[createdAt] = now
                it[updatedAt] = now
                it[expiresAt] = now
            } get CartTable.id

            logger.info("Inserted cart with ID: $cartId for user: $userId")
            cartId
        } catch (e: ExposedSQLException) {
            logger.error("Error creating cart for user: $userId", e)
            -1L // Indicate failure with a negative value
        }
    }

    override suspend fun getActiveCartByUserId(userId: Long): Cart? = dbQuery {
        CartTable
            .select { (CartTable.userId eq userId) and (CartTable.status eq CartStatus.ACTIVE) }
            .singleOrNull()?.let { toCartWithProducts(it) }
    }


    override suspend fun getCartById(cartId: Long): Cart? = dbQuery {
        CartTable.select { CartTable.id eq cartId }
            .singleOrNull()?.let { toCartWithProducts(it) }
    }

    private suspend fun toCartWithProducts(row: ResultRow): Cart {
        val cartId = row[CartTable.id]
        val items = getCartItems(cartId)
        return Cart(
            id = cartId,
            userId = row[CartTable.userId],
            status = row[CartTable.status],
            total = row[CartTable.total],
            currency = row[CartTable.currency],
            createdAt = row[CartTable.createdAt].toString(),
            updatedAt = row[CartTable.updatedAt].toString(),
            expiresAt = row[CartTable.expiresAt].toString(),
            items = items
        )
    }



    override suspend fun addItemToCart(
        cartId: Long,
        productId: Long,
        quantity: Int,
        price: Float
    ): Long = dbQuery {
        try {
            val now = CurrentDateTime()
            dbQuery {
                // Check if the cart exists and is active
                val cart = CartTable.select { CartTable.id eq cartId }.singleOrNull()
                if (cart == null || cart[CartTable.status] != CartStatus.ACTIVE) {
                    logger.warn("Cart is not active or does not exist: cartId=$cartId")
                    throw Exception("Cart is not active or does not exist")
                }

                // Check if the item already exists in the cart
                val existingItem = CartItemTable.select {
                    (CartItemTable.cartId eq cartId) and (CartItemTable.productId eq productId)
                }.singleOrNull()

                if (existingItem != null) {
                    // Update quantity
                    val newQuantity = existingItem[CartItemTable.quantity] + quantity
                    CartItemTable.update({ CartItemTable.id eq existingItem[CartItemTable.id] }) {
                        it[CartItemTable.quantity] = newQuantity
                        it[updatedAt] = now
                    }
                    logger.info("Updated quantity for cart item: cartItemId=${existingItem[CartItemTable.id]}")
                } else {
                    // Add new item
                    CartItemTable.insert {
                        it[CartItemTable.cartId] = cartId
                        it[CartItemTable.productId] = productId
                        it[CartItemTable.quantity] = quantity
                        it[CartItemTable.price] = price
                        it[createdAt] = now
                        it[updatedAt] = now
                    }
                    logger.info("Inserted new item to cart: cartId=$cartId, productId=$productId")
                }
                updateCartTotal(cartId)
            }

            cartId // Return cart ID as success indicator
        } catch (e: Exception) {
            logger.error("Error adding item to cart: cartId=$cartId, productId=$productId", e)
            -1L // Indicate failure with a negative value
        }
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean = dbQuery {
        val updatedRows = CartItemTable.update({ CartItemTable.id eq cartItemId }) {
            it[CartItemTable.quantity] = quantity
            it[updatedAt] = CurrentDateTime.now()
        }
        if (updatedRows > 0) {
            val cartId = CartItemTable.select { CartItemTable.id eq cartItemId }
                .single()[CartItemTable.cartId]
            updateCartTotal(cartId)
            true
        } else {
            false
        }
    }

    override suspend fun removeCartItem(cartItemId: Long): Boolean = dbQuery {
        val cartId = CartItemTable.select { CartItemTable.id eq cartItemId }
            .singleOrNull()?.get(CartItemTable.cartId)
        val deletedRows = CartItemTable.deleteWhere { CartItemTable.id eq cartItemId }
        if (deletedRows > 0 && cartId != null) {
            updateCartTotal(cartId)
            true
        } else {
            false
        }
    }

    override suspend fun getCartItems(cartId: Long): List<CartItem> = dbQuery {
        (CartItemTable innerJoin ProductsTable)
            .select { CartItemTable.cartId eq cartId }
            .map { row ->
                val productId = row[CartItemTable.productId]
                CartItem(
                    id = row[CartItemTable.id],
                    cartId = row[CartItemTable.cartId],
                    productId = productId,
                    quantity = row[CartItemTable.quantity],
                    price = row[CartItemTable.price],
                    createdAt = row[CartItemTable.createdAt].toString(),
                    updatedAt = row[CartItemTable.updatedAt].toString(),
                    productName = row[ProductsTable.name],
                    productDescription = row[ProductsTable.description],
                    productImages = getProductImages(productId) // Add this line
                )
            }
    }

    private fun getProductImages(productId: Long): List<String> {
        return ProductImagesTable
            .select { ProductImagesTable.productId eq productId }
            .map { it[ProductImagesTable.imageUrl] }
    }

    override suspend fun clearCart(cartId: Long): Boolean = dbQuery {
        val deletedRows = CartItemTable.deleteWhere { CartItemTable.cartId eq cartId }
        updateCartTotal(cartId)
        deletedRows > 0
    }


    override suspend fun updateCartStatus(cartId: Long, status: CartStatus): Boolean = dbQuery {
        val updatedRows = CartTable.update({ CartTable.id eq cartId }) {
            it[CartTable.status] = status
            it[updatedAt] = CurrentDateTime()
        }
        updatedRows > 0
    }

    override suspend fun getCartItemById(cartItemId: Long): CartItem? = dbQuery {
        (CartItemTable innerJoin ProductsTable)
            .select { CartItemTable.id eq cartItemId }
            .singleOrNull()?.let { row ->
                CartItem(
                    id = row[CartItemTable.id],
                    cartId = row[CartItemTable.cartId],
                    productId = row[CartItemTable.productId],
                    quantity = row[CartItemTable.quantity],
                    price = row[CartItemTable.price],
                    createdAt = row[CartItemTable.createdAt].toString(),
                    updatedAt = row[CartItemTable.updatedAt].toString(),
                    productName = row[ProductsTable.name],
                    productDescription = row[ProductsTable.description],
                    productImages = emptyList()
                )
            }
    }

    private fun updateCartTotal(cartId: Long) {
        val now = CurrentDateTime()

        // Calculate total using Float for price and Int for quantity
        val total = CartItemTable
            .slice((CartItemTable.price * CartItemTable.quantity.castTo(FloatColumnType())).sum())
            .select { CartItemTable.cartId eq cartId }
            .singleOrNull()
            ?.get((CartItemTable.price * CartItemTable.quantity.castTo(FloatColumnType())).sum())
            ?: 0.0f

        // Update CartTable with the computed total
        CartTable.update({ CartTable.id eq cartId }) {
            it[CartTable.total] = total
            it[updatedAt] = now
        }

        logger.info("Updated cart total for cartId=$cartId to $total")
    }



}