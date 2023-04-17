package com.kosim97.mulgaTalkTalk.data.remote.model

import com.google.gson.annotations.SerializedName

data class ApiData(
    @SerializedName("ListNecessariesPricesService")
    var list: ListData
)

data class ListData(
    @SerializedName("row")
    var row: ArrayList<ResultData>,
)

data class ResultData(
    @SerializedName("M_NAME")
    var marketName: String,
    @SerializedName("A_NAME")
    var productName: String,
    @SerializedName("A_UNIT")
    var productUnit: String,
    @SerializedName("A_PRICE")
    var productPrice: String,
    @SerializedName("P_DATE")
    var updateDate: String,
    @SerializedName("ADD_COL")
    var productInfo: String,
    @SerializedName("P_YEAR_MONTH")
    var updateMonth: String,
)