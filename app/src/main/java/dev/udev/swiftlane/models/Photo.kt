package dev.udev.swiftlane.models

import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(indices = [Index("id")], primaryKeys = ["userId"])
data class Photo(@SerializedName("id") val id: Int,
                 @SerializedName("largeImageURL") val imageUrl: String,
                 @SerializedName("imageWidth") val width: Int,
                 @SerializedName("imageHeight") val height: Int,
                 @SerializedName("tags") val tags: String,
                 @SerializedName("type") val type: String,
                 @SerializedName("user") val user: String,
                 @SerializedName("user_id") val userId: Long,
                 @SerializedName("likes") val likesNumber: Int)