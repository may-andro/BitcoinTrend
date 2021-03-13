package com.mayandro.datasource.di

import com.mayandro.datasource.factory.DataSourceFactory
import com.mayandro.datasource.factory.DataSourceFactoryImpl
import com.mayandro.remote.RemoteDataSource
import org.koin.dsl.module

val dataSourceModule = module() {
    single {
        provideDataSourceFactory(
            remoteDataSource = get()
        )
    }
}

private fun provideDataSourceFactory(
    remoteDataSource: RemoteDataSource,
): DataSourceFactory {
    return DataSourceFactoryImpl(remoteDataSource = remoteDataSource)
}