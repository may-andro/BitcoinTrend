package com.mayandro.remote.retrofit

import com.mayandro.remote.apimodel.ChartResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApi {

    @GET("charts/{chartName}")
    suspend fun getMarketPriceChart(
        @Path("chartName") chartName: String?,
        @Query("timespan") timespan: String?,
        @Query("rollingAverage") rollingAverage: String?,
        @Query("format") format: String?
    ) : Response<ChartResponse>
}