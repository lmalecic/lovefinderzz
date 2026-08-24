package com.lmalecic.lovefinderzz.api

import com.google.gson.annotations.SerializedName

data class ApiResponseInfo(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val previous: String?
)

data class ApiResponse<T>(
    val info: ApiResponseInfo,
    val results: List<T>
)
