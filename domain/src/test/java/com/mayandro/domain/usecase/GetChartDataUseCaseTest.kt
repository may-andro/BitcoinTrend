package com.mayandro.domain.usecase

import com.mayandro.common.network.NetworkStatus
import com.mayandro.domain.repository.ChartRepository
import com.mayandro.remote.apimodel.ChartResponse
import io.mockk.*
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetChartDataUseCaseTest {
    private val chartRepository: ChartRepository = mockk()

    private lateinit var getChartDataUseCase: GetChartDataUseCase

    @Before
    fun setUp() {
        getChartDataUseCase = GetChartDataUseCase(
            chartRepository
        )
    }

    @Test
    fun getChartDataUseCaseTest() {
        runBlocking {
            val chartName = ""
            val timespan = ""
            val rollingAverage = ""
            val format = ""

            val param = GetChartDataUseCase.Param(
                timespan = timespan,
                rollingAverage = rollingAverage,
                format = format,
                chartName = chartName
            )

            val mockServerResponse = NetworkStatus.Success(
                ChartResponse(
                    status = "",
                    name = "test",
                    description = "test",
                    period = "",
                    unit = "",
                    values = listOf()
                )
            )

            //STUB calls
            coEvery { chartRepository.getMarketChart(
                chartName = chartName,
                timespan = timespan,
                rollingAverage = rollingAverage,
                format = format
            ) } returns mockServerResponse

            //Execute the code
            val flow = getChartDataUseCase.run(param)

            val result = flow.single()
            assertEquals(result, mockServerResponse)
        }

    }
}