package com.mayandro.remote

import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse
import com.mayandro.remote.retrofit.RetrofitApi
import com.mayandro.remote.utils.ApiResponseHandler.safeApiCall

class RemoteDataSourceImpl(
    private val retrofit: RetrofitApi
): RemoteDataSource {
    override suspend fun getMarketPriceChart(
        chartName: String,
        timespan: String?,
        rollingAverage: String?,
        format: String?
    ): NetworkStatus<ChartResponse> {
        return safeApiCall {
            retrofit.getMarketPriceChart(
                chartName = chartName,
                timespan = timespan,
                rollingAverage = rollingAverage,
                format = format
            )
        }
    }
}