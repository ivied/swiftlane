package dev.udev.swiftlane.db

import android.util.SparseIntArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.udev.swiftlane.models.Photo
import dev.udev.swiftlane.models.QueryResult
import java.util.*

@Dao
abstract class PicData {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg photo: Photo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertPhotos(photos: List<Photo>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun createPhotoIfNotExists(photo: Photo): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(result: QueryResult)

    @Query("SELECT * FROM QueryResult WHERE `query` = :query")
    abstract fun search(query: String): LiveData<QueryResult>

    fun loadOrdered(photoIds: List<Int>): LiveData<List<Photo>> {
        val order = SparseIntArray()
        photoIds.withIndex().forEach {
            order.put(it.value, it.index)
        }
        return Transformations.map(loadById(photoIds), fun(photos: List<Photo>): List<Photo>? {
            Collections.sort(photos) { r1, r2 ->
                val pos1 = order.get(r1.id)
                val pos2 = order.get(r2.id)
                pos1 - pos2
            }
            return photos
        })
    }

    @Query("SELECT * FROM Photo WHERE id in (:photoIds)")
    protected abstract fun loadById(photoIds: List<Int>): LiveData<List<Photo>>
}