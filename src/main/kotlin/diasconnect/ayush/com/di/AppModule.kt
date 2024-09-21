package diasconnect.ayush.com.di

import diasconnect.ayush.com.dao.graphql.UserDaoGraph
import diasconnect.ayush.com.dao.graphql.UserRepository
import org.koin.dsl.module

val appModule = module {

    single { UserDaoGraph() }
    single { UserRepository(get()) }





}