package dev.udev.swiftlane.models

import androidx.room.Entity

@Entity(primaryKeys = ["query"])
data class QueryResult(val query: String, val picIds: List<Int>, val total: Int, val next: Int?)