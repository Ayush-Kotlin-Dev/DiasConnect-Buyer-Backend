package diasconnect.ayush.com.di

import diasconnect.ayush.com.dao.user.UserDao
import diasconnect.ayush.com.dao.user.UserDaoImpl
import diasconnect.ayush.com.repository.auth.AuthRepository
import diasconnect.ayush.com.repository.auth.AuthRepositoryImpl
import org.koin.dsl.module

val appModule = module {


    single <UserDao>{ UserDaoImpl() }
    single  <AuthRepository>{ AuthRepositoryImpl(get()) }

}