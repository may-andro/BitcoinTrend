package com.mayandro.remote.retrofit

import com.mayandro.remote.apimodel.ChartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

    @GET("/market-price")
    suspend fun getMarketPriceChart(
        @Query("timespan") timespan: String?,
        @Query("rollingAverage") rollingAverage: String?,
        @Query("format") format: String?
    ) : Response<ChartResponse>
}