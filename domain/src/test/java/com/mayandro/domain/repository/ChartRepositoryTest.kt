package com.mayandro.domain.repository

import com.mayandro.common.network.NetworkStatus
import com.mayandro.datasource.factory.DataSourceFactory
import com.mayandro.remote.RemoteDataSource
import com.mayandro.remote.apimodel.ChartResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ChartRepositoryTest {

    private val remoteDataSource: RemoteDataSource = mockk()
    private val dataSourceFactory: DataSourceFactory = mockk()

    private lateinit var chartRepository: ChartRepository

    @Before
    fun setUp() {
        chartRepository = ChartRepositoryImpl(
            dataSourceFactory
        )
    }

    private fun networkResponse() = NetworkStatus.Success(
        ChartResponse(
            status = "",
            name = "test",
            description = "test",
            period = "",
            unit = "",
            values = listOf()
        )
    )


    @Test
    fun getChartTest() = runBlocking {
        val mockServerData = networkResponse()

        val chartName = ""
        val timespan = ""
        val rollingAverage = ""
        val format = ""


        //STUB calls
        coEvery { remoteDataSource.getMarketPriceChart(
            timespan = timespan,
            rollingAverage = rollingAverage,
            format = format
        ) } returns mockServerData

        coEvery { dataSourceFactory.retrieveRemoteDataStore() } returns remoteDataSource

        //Execute the code
        val getProductDetail = chartRepository.getMarketChart(
            timespan = timespan,
            rollingAverage = rollingAverage,
            format = format
        )

        //Verify
        coVerify { dataSourceFactory.retrieveRemoteDataStore() }

        assertEquals(getProductDetail, mockServerData)
    }
}