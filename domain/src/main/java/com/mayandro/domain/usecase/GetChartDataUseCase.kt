package com.mayandro.domain.usecase

import com.mayandro.common.network.NetworkStatus
import com.mayandro.domain.repository.ChartRepository
import com.mayandro.remote.apimodel.ChartResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetChartDataUseCase(
    private val chartRepository: ChartRepository
): UseCase<GetChartDataUseCase.Param, ChartResponse>() {

    data class Param(
        val chartName: String?,
        val timespan: String?,
        val rollingAverage: String?,
        val format: String?,
    )

    override suspend fun run(param: Param): Flow<NetworkStatus<ChartResponse>> {
        return flow { emit(chartRepository.getMarketChart(
            timespan = param.timespan,
            rollingAverage = param.rollingAverage,
            format = param.format
        )) }
    }
}