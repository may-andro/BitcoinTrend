package com.mayandro.bitcointrend.ui.home.dashboard.uimodel

import com.github.mikephil.charting.data.Entry

data class ChartDataModel(
    val yAxisMax: Float,
    val yAxisMin: Float,
    val xAxisList: List<String>,
    val entries: List<Entry>
)

data class SelectorModel(
    val title: String,
    val value: String,
)