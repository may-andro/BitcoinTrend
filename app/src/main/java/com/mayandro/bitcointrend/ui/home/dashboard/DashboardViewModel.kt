package com.mayandro.bitcointrend.ui.home.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.mayandro.bitcointrend.ui.base.BaseViewModel
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.SelectorModel
import com.mayandro.common.network.NetworkStatus
import com.mayandro.domain.usecase.GetChartDataUseCase
import com.mayandro.remote.apimodel.ChartResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DashboardViewModel(
    private val getChartDataUseCase: GetChartDataUseCase
): BaseViewModel<DashboardInteractor>() {

    private var chartResponseLiveData: MutableLiveData<NetworkStatus<ChartResponse>> = MutableLiveData()
    var selectedStatsSelectorModel: SelectorModel = SelectorModel(title = "Market Price", value = "market-price")
    var selectedFilterSelectorModel: SelectorModel =  SelectorModel(title = "This Week", value = "1weeks")

    val chartResponse : LiveData<NetworkStatus<ChartResponse>>
        get() = chartResponseLiveData

    fun getChartData(
        chartName: String,
        timespan: String?,
        rollingAverage: String?,
        format: String?,
    ) {
        val param = GetChartDataUseCase.Param(
            chartName = chartName,
            timespan = timespan,
            rollingAverage = rollingAverage,
            format = format
        )

        getChartDataUseCase.invoke(
            viewModelScope,
            param
        ) {
            chartResponseLiveData.postValue(it)
        }
    }

    fun uiStatsTypeSelectorList(): Flow<List<SelectorModel>> {
        return flow {
            val list = listOf(
                SelectorModel(
                    title = "Market Price",
                    value = "market-price"
                ),
                SelectorModel(
                    title = "Average Block Size",
                    value = "average-block-size"
                ),
                SelectorModel(
                    title = "Transactions per Day",
                    value = "transactions-per-second"
                )
            )
            emit(list)
        }
    }

    fun uiFilterList(): Flow<List<SelectorModel>> {
        return flow {
            val list = listOf(
                SelectorModel(
                    title = "This Week",
                    value = "1weeks"
                ),
                SelectorModel(
                    title = "Last one Month",
                    value = "4weeks"
                ),
                SelectorModel(
                    title = "Last six Months",
                    value = "25weeks"
                ),
                SelectorModel(
                    title = "Last one Year",
                    value = "50weeks"
                )
            )
            emit(list)
        }
    }
}