package dev.udev.swiftlane.network.responses

import com.google.gson.annotations.SerializedName
import dev.udev.swiftlane.models.Photo

class PicsListResponse(@SerializedName("totalHits") val totalHits: Int = 0,
                       @SerializedName("hits") val pics: List<Photo>,
                       @SerializedName("total") val total: Int) {
    var nextPage: Int? = null
}