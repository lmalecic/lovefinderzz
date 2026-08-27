package com.lmalecic.lovefinderzz.api

import com.google.gson.annotations.SerializedName

data class ApiResponseInfo(
    @SerializedName("count") val count: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("next") val next: String?,
    @SerializedName("prev") val previous: String?
)

data class ApiResponse<T>(
    @SerializedName("info") val info: ApiResponseInfo,
    @SerializedName("results") val results: List<T>
)
