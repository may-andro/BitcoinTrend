package com.mayandro.domain.repository

import com.mayandro.common.network.NetworkStatus
import com.mayandro.datasource.factory.DataSourceFactory
import com.mayandro.remote.apimodel.ChartResponse

class ChartRepositoryImpl (
    private val dataSourceFactory: DataSourceFactory
): ChartRepository {

    override suspend fun getMarketChart(
        timespan: String?,
        rollingAverage: String?,
        format: String?,
    ): NetworkStatus<ChartResponse> {
        return dataSourceFactory.retrieveRemoteDataStore().getMarketPriceChart(
            timespan, rollingAverage, format
        )
    }
}