package com.mayandro.bitcointrend.ui.home.dashboard

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mayandro.bitcointrend.R
import com.mayandro.bitcointrend.databinding.FragmentDashboardBinding
import com.mayandro.bitcointrend.ui.base.BaseFragment
import com.mayandro.bitcointrend.ui.home.dashboard.adapter.StatsAdapter
import com.mayandro.bitcointrend.ui.home.dashboard.uimodel.SelectorModel
import com.mayandro.bitcointrend.utils.ChartUtils
import com.mayandro.bitcointrend.utils.SpacesItemDecoration
import com.mayandro.common.LIST_FORMAT
import com.mayandro.common.dataandtime.formatUnixTimeLong
import com.mayandro.common.extensions.rotate
import com.mayandro.common.network.NetworkStatus
import com.mayandro.remote.apimodel.ChartResponse
import com.mayandro.remote.apimodel.ChartValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class DashboardFragment : BaseFragment<FragmentDashboardBinding>() {
    private val dashboardViewModel: DashboardViewModel by viewModel()

    private val chartUtils: ChartUtils by inject()

    override fun getViewBinding(): FragmentDashboardBinding = FragmentDashboardBinding.inflate(
        layoutInflater
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chartUtils.setUpChart(binding.lineChart)
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

    /*
     View Setups
    */
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

                        val selectedItem = list.indexOf(dashboardViewModel.selectedFilterSelectorModel)
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
        binding.textStatsCardValue.text = getString(R.string.loading)
        binding.textStatsCardLabel.text = getString(R.string.loading_message)
    }

    private fun showErrorUiState(error: String) {
        binding.imageRefresh.clearAnimation()
        (binding.recyclerViewStats.adapter as StatsAdapter).dataSet = emptyList()
        binding.textStatsCardValue.text = getString(R.string.error_title)
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
            dashboardViewModel.getChartEntries(values)
                .flowOn(Dispatchers.IO)
                .collect {
                    chartUtils.fillChart(binding.lineChart, it, name)
                }
        }
    }



    /*
     Server Calls
    */

    private fun callServerForData() {
        dashboardViewModel.getChartData(
            chartName = dashboardViewModel.selectedStatsSelectorModel.value,
            timespan = dashboardViewModel.selectedFilterSelectorModel.value,
            rollingAverage = "8hours",
            format = "json"
        )
        chartUtils.clearLineChart(binding.lineChart)
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

