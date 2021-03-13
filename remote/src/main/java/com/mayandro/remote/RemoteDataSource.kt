package com.mayandro.remote

import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse

interface RemoteDataSource {
    suspend fun getMarketPriceChart(
        timespan: String?,
        rollingAverage: String?,
        format: String?,
    ): NetworkStatus<ChartResponse>
}