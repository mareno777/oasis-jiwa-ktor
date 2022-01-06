package com.injilkeselamatan.di

import com.injilkeselamatan.data.UserRepository
import com.injilkeselamatan.data.UserRepositoryImpl
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("oasis_jiwa_db")
    }

    single<UserRepository> { UserRepositoryImpl(get()) }
}