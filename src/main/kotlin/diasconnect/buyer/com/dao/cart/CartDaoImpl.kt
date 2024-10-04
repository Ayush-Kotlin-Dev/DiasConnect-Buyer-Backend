package diasconnect.buyer.com.dao.cart

import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.model.*
import diasconnect.buyer.com.util.CurrentDateTime
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.times
import org.slf4j.LoggerFactory
import java.math.BigDecimal

class CartDaoImpl : CartDao {
    private val logger = LoggerFactory.getLogger(CartDaoImpl::class.java)

    override suspend fun createCart(userId: Long): Long = dbQuery {
        logger.info("Creating cart in DAO for user: $userId")
        try {
            val now = CurrentDateTime()
            val cartId = CartTable.insert {
                it[CartTable.userId] = userId
                it[status] = CartStatus.ACTIVE
                it[total] = BigDecimal.ZERO
                it[currency] = "USD"
                it[createdAt] = now
                it[updatedAt] = now
                it[expiresAt] = now
            } get CartTable.id

            logger.info("Inserted cart with ID: $cartId for user: $userId")
            cartId
        } catch (e: ExposedSQLException) {
            if (e.message?.contains("UNIQUE constraint failed") == true) {
                logger.info("Cart already exists for user: $userId. Retrieving existing cart ID.")
                val existingCartId = CartTable
                    .slice(CartTable.id)
                    .select { (CartTable.userId eq userId) and (CartTable.status eq CartStatus.ACTIVE) }
                    .singleOrNull()?.get(CartTable.id)
                if (existingCartId == null) {
                    logger.error("Failed to retrieve existing active cart ID for user: $userId")
                    throw RuntimeException("Failed to retrieve existing active cart ID")
                }
                existingCartId
            } else {
                logger.error("Error creating cart for user: $userId", e)
                throw e
            }
        }
    }

    override suspend fun getActiveCartByUserId(userId: Long): Cart? = dbQuery {
        CartTable.select { (CartTable.userId eq userId) and (CartTable.status eq CartStatus.ACTIVE) }
            .singleOrNull()?.let { toCart(it) }
    }

    override suspend fun getCartById(cartId: Long): Cart? = dbQuery {
        CartTable.select { CartTable.id eq cartId }
            .singleOrNull()?.let { toCart(it) }
    }

    override suspend fun addItemToCart(
        cartId: Long,
        productId: Long,
        quantity: Int,
        price: BigDecimal
    ): Long = dbQuery {
        val existingItem = CartItemTable.select {
            (CartItemTable.cartId eq cartId) and (CartItemTable.productId eq productId)
        }.singleOrNull()

        val cartItemId = if (existingItem != null) {
            CartItemTable.update({ CartItemTable.id eq existingItem[CartItemTable.id] }) {
                it[CartItemTable.quantity] = existingItem[CartItemTable.quantity] + quantity
                it[CartItemTable.price] = price
                it[updatedAt] = CurrentDateTime()
            }
            existingItem[CartItemTable.id]
        } else {
            CartItemTable.insert {
                it[CartItemTable.cartId] = cartId
                it[CartItemTable.productId] = productId
                it[CartItemTable.quantity] = quantity
                it[CartItemTable.price] = price
                it[createdAt] = CurrentDateTime()
                it[updatedAt] = CurrentDateTime()
            } get CartItemTable.id
        }

        updateCartTotal(cartId)
        cartItemId

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
        CartItemTable.select { CartItemTable.cartId eq cartId }
            .map { toCartItem(it) }
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
        CartItemTable.select { CartItemTable.id eq cartItemId }
            .singleOrNull()?.let { toCartItem(it) }
    }

    private suspend fun updateCartTotal(cartId: Long) = dbQuery {
        val total = CartItemTable
            .slice(Expression.build {
                (CartItemTable.price.castTo<BigDecimal>(DecimalColumnType(10, 2)) *
                        CartItemTable.quantity.castTo<BigDecimal>(DecimalColumnType(10, 2))).sum()
            })
            .select { CartItemTable.cartId eq cartId }
            .singleOrNull()
            ?.get(Expression.build {
                (CartItemTable.price.castTo<BigDecimal>(DecimalColumnType(10, 2)) *
                        CartItemTable.quantity.castTo<BigDecimal>(DecimalColumnType(10, 2))).sum()
            }) ?: BigDecimal.ZERO

        CartTable.update({ CartTable.id eq cartId }) {
            it[CartTable.total] = total
            it[updatedAt] = CurrentDateTime()
        }
    }

    private suspend fun toCart(row: ResultRow): Cart {
        val cartId = row[CartTable.id]
        val items = getCartItems(cartId)
        return Cart(
            id = cartId,
            userId = row[CartTable.userId],
            status = row[CartTable.status],
            total = row[CartTable.total].toString(),
            currency = row[CartTable.currency],
            createdAt = row[CartTable.createdAt].toString(),
            updatedAt = row[CartTable.updatedAt].toString(),
            expiresAt = row[CartTable.expiresAt].toString(),
            items = items
        )
    }

    private fun toCartItem(row: ResultRow): CartItem =
        CartItem(
            id = row[CartItemTable.id],
            cartId = row[CartItemTable.cartId],
            productId = row[CartItemTable.productId],
            quantity = row[CartItemTable.quantity],
            price = row[CartItemTable.price].toString(),
            createdAt = row[CartItemTable.createdAt].toString(),
            updatedAt = row[CartItemTable.updatedAt].toString()
        )
}