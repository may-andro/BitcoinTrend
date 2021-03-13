package com.mayandro.domain.repository

import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse

interface ChartRepository {
    suspend fun getMarketChart(
        timespan: String?,
        rollingAverage: String?,
        format: String?,
    ): NetworkStatus<ChartResponse>
}