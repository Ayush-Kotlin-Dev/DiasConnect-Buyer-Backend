package diasconnect.buyer.com.di

import diasconnect.buyer.com.dao.product.BuyerProductDao
import diasconnect.buyer.com.dao.product.BuyerProductDaoImpl
import diasconnect.buyer.com.dao.user.UserDao
import diasconnect.buyer.com.dao.user.UserDaoImpl
import diasconnect.buyer.com.graphql.ProductQuery
import diasconnect.buyer.com.repository.auth.AuthRepository
import diasconnect.buyer.com.repository.auth.AuthRepositoryImpl
import diasconnect.buyer.com.repository.product.BuyerProductRepository
import diasconnect.buyer.com.repository.product.BuyerProductRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }
    single<BuyerProductDao> { BuyerProductDaoImpl() }
    single<BuyerProductRepository> { BuyerProductRepositoryImpl(get()) }
    single { ProductQuery(get()) }


}