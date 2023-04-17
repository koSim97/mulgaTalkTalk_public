package com.kosim97.mulgaTalkTalk.data

import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName

data class HomeDetail(
    @SerializedName("ListNecessariesPricesService")
    var list: DetailData,
)

data class DetailData(
    @SerializedName("row")
    var row: ArrayList<Detail>,
//    @SerializedName("RESULT")
//    var result: ArrayList<ResultDetail>,
)

data class ResultDetail(
    @SerializedName("CODE")
    var code: String
)

data class Detail(
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

data class HomeData(
    val region: String,
    val img: Drawable,
)

data class PrefConst(
    val KEY_API_DATE: String = "KEY_API_DATE"
)

