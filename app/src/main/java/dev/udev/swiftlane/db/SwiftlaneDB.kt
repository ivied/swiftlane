package dev.udev.swiftlane.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.udev.swiftlane.models.Photo
import dev.udev.swiftlane.models.QueryResult

@Database(entities = [Photo::class, QueryResult::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class SwiftlaneDB: RoomDatabase() {
    abstract fun picData(): PicData
}