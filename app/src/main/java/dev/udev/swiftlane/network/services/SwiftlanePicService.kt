package dev.udev.swiftlane.network.services

import androidx.lifecycle.LiveData
import dev.udev.swiftlane.network.responses.BaseResponse
import dev.udev.swiftlane.network.responses.PicsListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SwiftlanePicService {
    @GET("api/")
    fun getPicsByKeyword(@Query("key") apiKey: String,
                         @Query("q") keyword: String,
                         @Query("page") page: Int):
            LiveData<BaseResponse<PicsListResponse>>
}