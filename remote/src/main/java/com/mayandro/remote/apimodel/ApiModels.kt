package com.mayandro.remote.apimodel

import com.google.gson.annotations.SerializedName

data class ChartResponse(
    @SerializedName("status") val status: String,
    @SerializedName("name") val name: String,
    @SerializedName("unit") val unit: String,
    @SerializedName("period") val period: String,
    @SerializedName("description") val description: String,
    @SerializedName("values") val values: List<ChartValue>,
)

data class ChartValue(
    @SerializedName("x") val x: Long,
    @SerializedName("y") val y: Float
)
