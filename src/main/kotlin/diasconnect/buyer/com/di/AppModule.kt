package diasconnect.buyer.com.di

import diasconnect.buyer.com.dao.user.UserDao
import diasconnect.buyer.com.dao.user.UserDaoImpl
import diasconnect.buyer.com.repository.auth.AuthRepository
import diasconnect.buyer.com.repository.auth.AuthRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }

}