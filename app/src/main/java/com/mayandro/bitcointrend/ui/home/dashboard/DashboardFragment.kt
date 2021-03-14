package com.mayandro.bitcointrend.ui.home.dashboard

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.mayandro.bitcointrend.R
import com.mayandro.bitcointrend.databinding.FragmentDashboardBinding
import com.mayandro.bitcointrend.ui.base.BaseFragment
import com.mayandro.bitcointrend.ui.home.dashboard.adapter.StatsAdapter
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.ChartDataModel
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.SelectorModel
import com.mayandro.bitcointrend.utils.SpacesItemDecoration
import com.mayandro.common.CHART_FORMAT
import com.mayandro.common.LIST_FORMAT
import com.mayandro.common.dataandtime.formatUnixTimeLong
import com.mayandro.common.extensions.rotate
import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse
import com.mayandro.remote.apimodel.ChartValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : BaseFragment<FragmentDashboardBinding>(), DashboardInteractor {
    private val dashboardViewModel: DashboardViewModel by viewModel()

    override fun getViewBinding(): FragmentDashboardBinding = FragmentDashboardBinding.inflate(
        layoutInflater
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashboardViewModel.viewInteractor = this

        setUpChart()
        setUpStatsSelector()
        setUpFilterSelector()
        setUpRecyclerView()
        setUpProfileImage()
        setUpInfoImage()
        setUpSearchImage()
        setUpFabClick()
        setUpRefreshClick()

        dashboardViewModel.chartResponse.observe(viewLifecycleOwner) {
            handleUiState(it)
        }

        if(savedInstanceState == null) callServerForData()
    }

    private fun setUpRefreshClick() {
        binding.imageRefresh.setOnClickListener {
            callServerForData()
        }
    }

    private fun setUpFabClick() {
        binding.fabDownload.setOnClickListener {
            showUpcomingDialog()
        }
    }

    /*
     View Setups
    */

    private fun setUpSearchImage() {
        binding.imageSearch.setOnClickListener {
            showUpcomingDialog()
        }
    }

    private fun setUpInfoImage() {
        binding.imageInfo.setOnClickListener {
            dashboardViewModel.chartResponse.value?.let {
                if(it.data?.name.isNullOrBlank() || it.data?.description.isNullOrBlank()) return@setOnClickListener
                showDialogMessage(
                    title = it.data?.name!!,
                    message = it.data?.description!!
                )
            }
        }
    }

    private fun setUpProfileImage() {
        binding.cardProfileImageContainer.setOnClickListener {
            showUpcomingDialog()
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewStats.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = StatsAdapter()
            addItemDecoration(SpacesItemDecoration(8))
        }
    }

    private fun setUpFilterSelector() {
        binding.imageFilter.setOnClickListener {
            lifecycleScope.launch {
                dashboardViewModel.uiFilterList()
                    .transform { list ->
                        val stringList = list.map {
                            it.title
                        }

                        var selectedItem = list.indexOf(dashboardViewModel.selectedFilterSelectorModel)
                        emit(Triple(list, stringList, selectedItem))
                    }
                    .flowOn(Dispatchers.Default)
                    .collect { triple ->
                        showFilterSelectorDialog(
                            triple.second, triple.first, triple.third
                        )
                    }
            }
        }
    }

    private fun setUpStatsSelector() {
        binding.textStatsLabel.setOnClickListener {
            lifecycleScope.launch {
                dashboardViewModel.uiStatsTypeSelectorList()
                    .transform { list ->
                        val stringList = list.map {
                            it.title
                        }

                        val selectedItem = list.indexOf(dashboardViewModel.selectedStatsSelectorModel)
                        emit(Triple(list, stringList, selectedItem))
                    }
                    .flowOn(Dispatchers.Default)
                    .collect { triple ->
                        showStatsSelectorDialog(
                            triple.second, triple.first, triple.third
                        )
                    }
            }

        }
    }

    private fun clearLineChart() {
        binding.lineChart.clear()
        binding.lineChart.clearAnimation()
    }

    private fun setUpChart() {
        binding.lineChart.apply {
            setViewPortOffsets(
                resources.getDimension(R.dimen.space2dp),
                0f,
                resources.getDimension(R.dimen.space2dp),
                resources.getDimension(R.dimen.space16dp)
            )
            // no description text
            description.isEnabled = false

            // enable touch gestures
            setTouchEnabled(false)

            dragDecelerationFrictionCoef = 0.9f

            // enable scaling and dragging
            isDragEnabled = false
            setScaleEnabled(false)
            setDrawGridBackground(false)
            isHighlightPerDragEnabled = false

            isLogEnabled = false

            legend.isEnabled = false

            // if disabled, scaling can be done on x- and y-axis separately
            setPinchZoom(false)

            binding.lineChart.axisRight.isEnabled = false
            binding.lineChart.axisLeft.isEnabled = false
        }
    }

    private fun handleUiState(networkStatusResponse: NetworkStatus<ChartResponse>?) {
        when (networkStatusResponse) {
            is NetworkStatus.Success -> {
                networkStatusResponse.data?.let {
                    showSuccessUiState(it)
                } ?: kotlin.run {
                    showErrorUiState("Null response found in the app")
                }
            }

            is NetworkStatus.Loading -> {
                showLoadingUiState()
            }

            is NetworkStatus.Error -> {
                showErrorUiState(networkStatusResponse.errorMessage?:"Something went wrong")
            }
        }

    }

    private fun showLoadingUiState() {
        binding.imageRefresh.rotate()
        binding.lineChart.isVisible = false
        binding.textStatsCardValue.text = "Loading...."
        binding.textStatsCardLabel.text = "Sit tight, we are getting the latest information for you."
    }

    private fun showErrorUiState(error: String) {
        binding.imageRefresh.clearAnimation()
        (binding.recyclerViewStats.adapter as StatsAdapter).dataSet = emptyList()
        binding.textStatsCardValue.text = "Oooops...."
        binding.textStatsCardLabel.text = error
        binding.lineChart.isVisible = false
    }

    private fun showSuccessUiState(chartResponse: ChartResponse) {
        binding.imageRefresh.clearAnimation()
        binding.textStatsCardLabel.text = "${chartResponse.values.last().y} ${chartResponse.unit}"
        binding.textStatsCardValue.text = chartResponse.name
        fillChartWithData(chartResponse.values, chartResponse.name)

        lifecycleScope.launch {
            flow{
                val recyclerViewList = mutableListOf<SelectorModel>()
                chartResponse.values.forEach { chartValue ->
                    recyclerViewList.add(
                        SelectorModel(
                            title = chartValue.x.formatUnixTimeLong(LIST_FORMAT),
                            value = "${chartValue.y} ${chartResponse.unit}"
                        )
                    )
                }
                emit(recyclerViewList)
            }.flowOn(Dispatchers.Default)
                .collect { list ->
                    (binding.recyclerViewStats.adapter as StatsAdapter).dataSet = list
                }
        }
    }

    private fun fillChartWithData(values: List<ChartValue>, name: String) {
        lifecycleScope.launch {
            getChartEntries(values)
                .flowOn(Dispatchers.IO)
                .collect {
                    fillChart(it, name)
                }
        }
    }

    private fun getChartEntries(values: List<ChartValue>): Flow<ChartDataModel> {
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

    private fun fillChart(chartDataModel: ChartDataModel, name: String) {
        getXAxisConfig(chartDataModel.xAxisList)
        getYAxisConfig(chartDataModel.yAxisMax, chartDataModel.yAxisMin)
        val lineDataSet = getLineDataSet(entries = chartDataModel.entries, chartName = name)

        val lineData = LineData(lineDataSet)
        lineData.setValueTextColor(Color.WHITE)
        lineData.setValueTextSize(9f)
        lineDataSet.setDrawFilled(true)
        val fillGradient = ContextCompat.getDrawable(requireContext(), R.drawable.white_gradient)
        lineDataSet.fillDrawable = fillGradient

        binding.lineChart.apply {
            data = lineData
            animateY(100)
            invalidate()
            isVisible = true
        }
    }

    private fun getXAxisConfig(xAxisList: List<String>) {
        val xAxis: XAxis = binding.lineChart.xAxis
        xAxis.apply {
            axisLineWidth = 2f
            axisLineColor = ContextCompat.getColor(requireContext(), R.color.white)
            position = XAxis.XAxisPosition.BOTTOM
            isGranularityEnabled = false
            granularity = 1f
            textSize = 11f

            textColor = Color.WHITE
            setDrawGridLines(false)
            setDrawAxisLine(false)
            setAvoidFirstLastClipping(true)
            setLabelCount(5, false)

            valueFormatter = IndexAxisValueFormatter(xAxisList)
        }
    }

    private fun getYAxisConfig(maxValue: Float, minValue: Float) {
        val leftAxis: YAxis = binding.lineChart.axisLeft
        leftAxis.apply {
            textColor = ColorTemplate.getHoloBlue()
            axisMaximum = maxValue * 1.25f
            axisMinimum = minValue * 0.55f
            setDrawGridLines(false)
            isGranularityEnabled = true
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        }
    }

    private fun getLineDataSet(entries: List<Entry>, chartName: String): LineDataSet {
        val lineDataSet = LineDataSet(entries, chartName)

        lineDataSet.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = ContextCompat.getColor(requireContext(), R.color.white)

            setDrawCircleHole(false)
            setDrawCircles(false)
            setDrawValues(false)
            setDrawIcons(false)
            setDrawFilled(false)

            mode = LineDataSet.Mode.CUBIC_BEZIER
            cubicIntensity = 0.2f
            valueTextSize = 9f
            lineWidth = 3f
            fillAlpha = 65
            fillColor = ContextCompat.getColor(requireContext(), R.color.white)
            highLightColor = ContextCompat.getColor(requireContext(), R.color.white)

            val formatter = IndexAxisValueFormatter()
            fillFormatter = IFillFormatter { _, _ ->
                binding.lineChart.axisLeft.axisMinimum
            }
        }

        return lineDataSet
    }

    /*
     Server Calls
    */

    private fun callServerForData() {
        dashboardViewModel.getChartData(
            chartName = dashboardViewModel.selectedStatsSelectorModel.value,
            timespan = dashboardViewModel.selectedFilterSelectorModel?.value ?: "4weeks",
            rollingAverage = "8hours",
            format = "json"
        )
        clearLineChart()
    }

    /*
     Dialog for the screen
    */

    private fun showStatsSelectorDialog(
        dialogList: List<String>,
        list: List<SelectorModel>,
        selectedItem: Int
    ) {
        dialogUtils.showListBottomSheet(
            context = requireContext(),
            title = "Select Popular Stats",
            message = "Here are popular stats available. Select to view information about the stat",
            list = dialogList,
            selectedItem = selectedItem,
            onItemSelected = {
                dashboardViewModel.selectedStatsSelectorModel = list[it]
                callServerForData()
            }
        )
    }

    private fun showFilterSelectorDialog(
        dialogList: List<String>,
        list: List<SelectorModel>,
        selectedItem: Int
    ) {
        dialogUtils.showListBottomSheet(
            context = requireContext(),
            title = "Select Filter",
            message = "Here are various option you can choose",
            list = dialogList,
            selectedItem = selectedItem,
            onItemSelected = {
                dashboardViewModel.selectedFilterSelectorModel = list[it]
                callServerForData()
            }
        )
    }

    private fun showUpcomingDialog() {
        showDialogMessage(
            title = "Coming soon ...",
            message = "This feature will be coming soon. Keep an eye on future updates."
        )
    }
}

