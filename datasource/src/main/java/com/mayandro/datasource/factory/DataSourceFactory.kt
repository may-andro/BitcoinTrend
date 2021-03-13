package com.mayandro.datasource.factory

import com.mayandro.remote.RemoteDataSource

interface DataSourceFactory  {
    fun retrieveRemoteDataStore(): RemoteDataSource
}