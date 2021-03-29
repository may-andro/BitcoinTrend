package com.mayandro.bitcointrend.ui.home.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.SelectorModel
import com.mayandro.bitcointrend.utils.ChartDataModel
import com.mayandro.common.CHART_FORMAT
import com.mayandro.common.dataandtime.formatUnixTimeLong
import com.mayandro.common.network.NetworkStatus
import com.mayandro.domain.usecase.GetChartDataUseCase
import com.mayandro.remote.apimodel.ChartResponse
import com.mayandro.remote.apimodel.ChartValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DashboardViewModel(
    private val getChartDataUseCase: GetChartDataUseCase
): ViewModel() {

    private var chartResponseLiveData: MutableLiveData<NetworkStatus<ChartResponse>> = MutableLiveData()
    var selectedStatsSelectorModel: SelectorModel = SelectorModel(title = "Market Price", value = "market-price")
    var selectedFilterSelectorModel: SelectorModel =  SelectorModel(title = "This Week", value = "1weeks")

    init {
        chartResponseLiveData.postValue(NetworkStatus.Loading())
    }

    val chartResponse : LiveData<NetworkStatus<ChartResponse>>
        get() = chartResponseLiveData

    fun getChartData(
        chartName: String,
        timespan: String?,
        rollingAverage: String?,
        format: String?,
    ) {
        chartResponseLiveData.postValue(NetworkStatus.Loading())

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

    fun getChartEntries(values: List<ChartValue>): Flow<ChartDataModel> {
        return flow {
            val entries: MutableList<Entry> = mutableListOf()
            val xAxisList: MutableList<String> = mutableListOf()
            var yAxisMax = 0f
            var yAxisMin = -1f

            values.forEach {
                xAxisList.add(it.x.formatUnixTimeLong(CHART_FORMAT))
                //Find Max
                if (yAxisMax < it.y) yAxisMax = it.y
                //Find Min
                if (yAxisMin == -1f || yAxisMin > it.y) yAxisMin = it.y

                val entry = Entry((xAxisList.size - 1).toFloat(), it.y)
                entries.add(entry)
            }

            emit(
                ChartDataModel(
                    yAxisMax = yAxisMax,
                    yAxisMin = yAxisMin,
                    xAxisList = xAxisList,
                    entries = entries
                )
            )
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