package diasconnect.buyer.com.di

import diasconnect.buyer.com.dao.cart.CartDao
import diasconnect.buyer.com.dao.cart.CartDaoImpl
import diasconnect.buyer.com.dao.order.OrderDAO
import diasconnect.buyer.com.dao.order.OrderDAOImpl
import diasconnect.buyer.com.dao.product.BuyerProductDao
import diasconnect.buyer.com.dao.product.BuyerProductDaoImpl
import diasconnect.buyer.com.dao.user.UserDao
import diasconnect.buyer.com.dao.user.UserDaoImpl
import diasconnect.buyer.com.graphql.CartQuery
import diasconnect.buyer.com.graphql.OrderMutation
import diasconnect.buyer.com.graphql.OrderQuery
import diasconnect.buyer.com.graphql.ProductQuery
import diasconnect.buyer.com.repository.cart.CartRepositoryImpl
import diasconnect.buyer.com.repository.auth.AuthRepository
import diasconnect.buyer.com.repository.auth.AuthRepositoryImpl
import diasconnect.buyer.com.repository.cart.CartRepository
import diasconnect.buyer.com.repository.order.OrderRepository
import diasconnect.buyer.com.repository.order.OrderRepositoryImpl
import diasconnect.buyer.com.repository.product.BuyerProductRepository
import diasconnect.buyer.com.repository.product.BuyerProductRepositoryImpl
import org.koin.dsl.module

val appModule = module {

    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get(),get()) }
    single<BuyerProductDao> { BuyerProductDaoImpl() }
    single<BuyerProductRepository> { BuyerProductRepositoryImpl(get()) }
    single { ProductQuery(get()) }
    single < CartDao>{ CartDaoImpl() }
    single <CartRepository>{ CartRepositoryImpl(get()) }
    single { CartQuery(get()) }

    single <OrderDAO>{ OrderDAOImpl() }
    single { OrderQuery(get()) }
    single { OrderMutation(get()) }
    single <OrderRepository>{ OrderRepositoryImpl(get()) }
}