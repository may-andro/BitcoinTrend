package com.mayandro.bitcointrend

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mayandro.bitcointrend.ui.home.dashboard.DashboardViewModel
import com.mayandro.common.network.NetworkStatus
import com.mayandro.domain.usecase.GetChartDataUseCase
import com.mayandro.remote.apimodel.ChartResponse
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.flow
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class DashboardViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getChartDataUseCase: GetChartDataUseCase = mockk(relaxed = true)

    private lateinit var viewModel: DashboardViewModel

    @ExperimentalCoroutinesApi
    @ObsoleteCoroutinesApi
    @Before
    fun setup() {
        viewModel = DashboardViewModel(getChartDataUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun stateLoading() {
        testCoroutineRule.runBlockingTest {

            val chartName = ""
            val timespan = ""
            val rollingAverage = ""
            val format = ""

            val param = GetChartDataUseCase.Param(chartName, timespan, rollingAverage, format)

            val serverResponse = ChartResponse(
                status = "",
                name = "test",
                description = "test",
                period = "",
                unit = "",
                values = listOf()
            )
            val networkServerResponse = NetworkStatus.Success(serverResponse)
            val flowResponse = flow { emit(networkServerResponse) }

            val mockedObserver : Observer<NetworkStatus<ChartResponse>> = spyk(Observer { })
            viewModel.chartResponse.observeForever(mockedObserver)

            //STUB calls
            coEvery { getChartDataUseCase.run(param) } returns flowResponse

            //Execute the code
            viewModel.getChartData(chartName, timespan, rollingAverage, format)

            //Verify
            assertThat(viewModel.chartResponse.value is NetworkStatus.Loading).isTrue()
        }
    }
}