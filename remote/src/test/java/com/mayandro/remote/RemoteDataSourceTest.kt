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
    private val summaryResponse = ChartResponse(
        status = "",
        name = "",
        unit = "",
        period = "",
        description = "",
        values = listOf()
    )

    private val networkSummaryResponse = NetworkStatus.Success(summaryResponse)

    @Test
    fun getMarketPriceChart() {
        val timeSpan = ""
        val rollingAverage = ""
        val format = ""


        val remoteDataSource = mockk<RemoteDataSourceImpl>()
        //STUB calls
        coEvery { remoteDataSource.getMarketPriceChart(timeSpan, rollingAverage, format) } returns networkSummaryResponse

        //Execute the code
        val result = runBlocking { remoteDataSource.getMarketPriceChart(timeSpan, rollingAverage, format) }

        //Verify
        coVerify { remoteDataSource.getMarketPriceChart(timeSpan, rollingAverage, format) }

        assertEquals(networkSummaryResponse, result)
    }

    @Test
    fun testSafeApiCallSuccessfull() {
        mockkObject(ApiResponseHandler)

        val apiRequest = mockk<Response<ChartResponse>>()

        val expectedResponse =  NetworkStatus.Success(summaryResponse)

        //STUB calls
        every { apiRequest.body() } returns summaryResponse
        every { apiRequest.isSuccessful } returns true
        coEvery { ApiResponseHandler.safeApiCall{ apiRequest } } returns expectedResponse

        val result = runBlocking { ApiResponseHandler.safeApiCall{ apiRequest } }

        assertEquals(expectedResponse.data, result.data)
    }

    @Test
    fun testSafeApiCallFailed() {
        mockkObject(ApiResponseHandler)

        val apiRequest = mockk<Response<ChartResponse>>()

        val expectedResponse =  NetworkStatus.Error(UNKNOWN_NETWORK_EXCEPTION, data = summaryResponse)

        //STUB calls
        every { apiRequest.isSuccessful } returns false
        coEvery { ApiResponseHandler.safeApiCall{ apiRequest } } returns expectedResponse

        //Execute the code
        val result = runBlocking { ApiResponseHandler.safeApiCall{ apiRequest } }

        //Verify
        assertEquals(expectedResponse.errorMessage, result.errorMessage)
    }
}