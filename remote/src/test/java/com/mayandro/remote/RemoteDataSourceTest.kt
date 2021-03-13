package com.mayandro.remote

import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse
import com.mayandro.remote.utils.ApiResponseHandler
import com.mayandro.remote.utils.UNKNOWN_NETWORK_EXCEPTION
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response

@RunWith(JUnit4::class)
class RemoteDataSourceTest {
    private val chartResponse = ChartResponse(
        status = "",
        name = "",
        unit = "",
        period = "",
        description = "",
        values = listOf()
    )

    private val networkchartResponse = NetworkStatus.Success(chartResponse)

    @Test
    fun getMarketPriceChart() {
        val chartName = ""
        val timeSpan = ""
        val rollingAverage = ""
        val format = ""


        val remoteDataSource = mockk<RemoteDataSourceImpl>()
        //STUB calls
        coEvery { remoteDataSource.getMarketPriceChart(chartName, timeSpan, rollingAverage, format) } returns networkchartResponse

        //Execute the code
        val result = runBlocking { remoteDataSource.getMarketPriceChart(chartName, timeSpan, rollingAverage, format) }

        //Verify
        coVerify { remoteDataSource.getMarketPriceChart(chartName, timeSpan, rollingAverage, format) }

        assertEquals(networkchartResponse, result)
    }

    @Test
    fun testSafeApiCallSuccessfull() {
        mockkObject(ApiResponseHandler)

        val apiRequest = mockk<Response<ChartResponse>>()

        val expectedResponse =  NetworkStatus.Success(chartResponse)

        //STUB calls
        every { apiRequest.body() } returns chartResponse
        every { apiRequest.isSuccessful } returns true
        coEvery { ApiResponseHandler.safeApiCall{ apiRequest } } returns expectedResponse

        val result = runBlocking { ApiResponseHandler.safeApiCall{ apiRequest } }

        assertEquals(expectedResponse.data, result.data)
    }

    @Test
    fun testSafeApiCallFailed() {
        mockkObject(ApiResponseHandler)

        val apiRequest = mockk<Response<ChartResponse>>()

        val expectedResponse =  NetworkStatus.Error(UNKNOWN_NETWORK_EXCEPTION, data = chartResponse)

        //STUB calls
        every { apiRequest.isSuccessful } returns false
        coEvery { ApiResponseHandler.safeApiCall{ apiRequest } } returns expectedResponse

        //Execute the code
        val result = runBlocking { ApiResponseHandler.safeApiCall{ apiRequest } }

        //Verify
        assertEquals(expectedResponse.errorMessage, result.errorMessage)
    }
}