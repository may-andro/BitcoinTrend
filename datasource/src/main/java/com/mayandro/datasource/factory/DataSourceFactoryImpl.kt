package com.mayandro.datasource.factory

import com.mayandro.remote.RemoteDataSource

class DataSourceFactoryImpl (
    private val remoteDataSource: RemoteDataSource,
): DataSourceFactory {
    override fun retrieveRemoteDataStore(): RemoteDataSource {
        return remoteDataSource
    }
}