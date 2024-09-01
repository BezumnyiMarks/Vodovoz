package com.example.vodovoz.Data

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Products(
    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("TOVARY")
    val tovary: List<Tovary>
)

data class Tovary (
    @SerializedName("ID")
    val id: Long,

    @SerializedName("NAME")
    val name: String,

    @SerializedName("data")
    val data: List<Data>
)

data class Data (
    @SerializedName("ID")
    val id: String,

    @SerializedName("IBLOCK_ID")
    val iblockID: String,

    @SerializedName("DETAIL_PICTURE")
    val detailPicture: String,

    @SerializedName("EXTENDED_PRICE")
    val extendedPrice: List<ExtendedPrice>
)

data class ExtendedPrice (
    @SerializedName("PRICE")
    val price: Long,

    @SerializedName("OLD_PRICE")
    val oldPrice: Long,

    @SerializedName("QUANTITY_FROM")
    val quantityFrom: Long,

    @SerializedName("QUANTITY_TO")
    val quantityTo: Long
)