package diasconnect.buyer.com.dao.cart


import diasconnect.buyer.com.dao.DatabaseFactory.dbQuery
import diasconnect.buyer.com.dao.product.ProductsTable
import diasconnect.buyer.com.util.IdGenerator
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class CartDaoImpl : CartDao {
    override suspend fun createCart(userId: Long): Long = dbQuery {
        try {
            CartTable.insert {
                it[CartTable.userId] = userId
                it[CartTable.id] = IdGenerator.generateId()
            } get CartTable.id
        } catch (e: ExposedSQLException) {
            // Check if the exception is due to the unique constraint violation
            if (e.localizedMessage.contains("UNIQUE constraint failed")) {
                CartTable
                    .select { CartTable.userId eq userId }
                    .single()[CartTable.id]
            } else {
                throw e
            }
        }
    }

    override suspend fun getCart(userId: Long): CartRow? = dbQuery {
        CartTable.select { CartTable.userId eq userId }
            .mapNotNull { toCartRow(it) }
            .singleOrNull()
    }

    override suspend fun addItemToCart(cartId: Long, productId: Long, quantity: Int): Long = dbQuery {
        CartItemsTable.insert {
            it[CartItemsTable.cartId] = cartId
            it[CartItemsTable.productId] = productId
            it[CartItemsTable.quantity] = quantity
        } get CartItemsTable.id
    }

    override suspend fun updateCartItemQuantity(cartItemId: Long, quantity: Int): Boolean = dbQuery {
        CartItemsTable.update({ CartItemsTable.id eq cartItemId }) {
            it[CartItemsTable.quantity] = quantity
        } > 0
    }

    override suspend fun removeCartItem(cartItemId: Long): Boolean = dbQuery {
        CartItemsTable.deleteWhere { CartItemsTable.id eq cartItemId } > 0
    }

    override suspend fun getCartItems(cartId: Long): List<CartItemRow> = dbQuery {
        CartItemsTable.select { CartItemsTable.cartId eq cartId }
            .mapNotNull { toCartItemRow(it) }
    }

    override fun clearCart(cartId: Long): Boolean {
        return transaction {
            CartItemsTable.deleteWhere { CartItemsTable.cartId eq cartId } > 0
        }
    }

    private fun toCartRow(row: ResultRow) = CartRow(
        id = row[CartTable.id],
        userId = row[CartTable.userId],
        createdAt = row[CartTable.createdAt].toString(),
        updatedAt = row[CartTable.updatedAt].toString()
    )

    private fun toCartItemRow(row: ResultRow) = CartItemRow(
        id = row[CartItemsTable.id],
        cartId = row[CartItemsTable.cartId],
        productId = row[CartItemsTable.productId],
        quantity = row[CartItemsTable.quantity],
        createdAt = row[CartItemsTable.createdAt].toString(),
        updatedAt = row[CartItemsTable.updatedAt].toString()
    )
}