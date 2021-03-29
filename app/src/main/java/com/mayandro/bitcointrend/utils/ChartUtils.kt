package com.mayandro.bitcointrend.utils

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.mayandro.bitcointrend.R


data class ChartDataModel(
    val yAxisMax: Float,
    val yAxisMin: Float,
    val xAxisList: List<String>,
    val entries: List<Entry>
)

class ChartUtils {

    fun setUpChart(chartView: LineChart) {
        chartView.apply {
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

            axisRight.isEnabled = false
            axisLeft.isEnabled = false
        }
    }

    fun fillChart(chartView: LineChart, chartDataModel: ChartDataModel, name: String) {
        getXAxisConfig(chartView, chartDataModel.xAxisList)
        getYAxisConfig(chartView, chartDataModel.yAxisMax, chartDataModel.yAxisMin)
        val lineDataSet = getLineDataSet(chartView, entries = chartDataModel.entries, chartName = name)

        val lineData = LineData(lineDataSet)
        lineData.setValueTextColor(Color.WHITE)
        lineData.setValueTextSize(9f)
        lineDataSet.setDrawFilled(true)
        val fillGradient = ContextCompat.getDrawable(chartView.context, R.drawable.white_gradient)
        lineDataSet.fillDrawable = fillGradient

        chartView.apply {
            data = lineData
            animateY(100)
            invalidate()
            isVisible = true
        }
    }

    private fun getXAxisConfig(chartView: LineChart, xAxisList: List<String>) {
        val xAxis: XAxis = chartView.xAxis
        xAxis.apply {
            axisLineWidth = 2f
            axisLineColor = ContextCompat.getColor(chartView.context, R.color.white)
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

    private fun getYAxisConfig(chartView: LineChart, maxValue: Float, minValue: Float) {
        val leftAxis: YAxis = chartView.axisLeft
        leftAxis.apply {
            textColor = ColorTemplate.getHoloBlue()
            axisMaximum = maxValue * 1.25f
            axisMinimum = minValue * 0.55f
            setDrawGridLines(false)
            isGranularityEnabled = true
            setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
        }
    }

    private fun getLineDataSet(chartView: LineChart, entries: List<Entry>, chartName: String): LineDataSet {
        val lineDataSet = LineDataSet(entries, chartName)

        lineDataSet.apply {
            axisDependency = YAxis.AxisDependency.LEFT
            color = ContextCompat.getColor(chartView.context, R.color.white)

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
            fillColor = ContextCompat.getColor(chartView.context, R.color.white)
            highLightColor = ContextCompat.getColor(chartView.context, R.color.white)

            fillFormatter = IFillFormatter { _, _ ->
                chartView.axisLeft.axisMinimum
            }
        }

        return lineDataSet
    }

    fun clearLineChart(chartView: LineChart) {
        chartView.apply {
            clear()
            clearAnimation()
        }
    }
}