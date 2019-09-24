package dev.udev.swiftlane.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import dev.udev.swiftlane.db.PicData
import dev.udev.swiftlane.db.SwiftlaneDB
import dev.udev.swiftlane.infrastructure.BaseExecutors
import dev.udev.swiftlane.livedata.EmptyLiveData
import dev.udev.swiftlane.models.Payload
import dev.udev.swiftlane.models.Photo
import dev.udev.swiftlane.models.QueryResult
import dev.udev.swiftlane.network.responses.BaseResponse
import dev.udev.swiftlane.network.responses.PicsListResponse
import dev.udev.swiftlane.network.services.SwiftlanePicService
import dev.udev.swiftlane.utils.Constants
import dev.udev.swiftlane.utils.Limiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PhotoRepository @Inject constructor(private val executors: BaseExecutors,
                                          private val database: SwiftlaneDB,
                                          private val picData: PicData,
                                          private val swiftlanePicService: SwiftlanePicService) {

    private val listLimiter = Limiter<String>(10, TimeUnit.SECONDS)

    fun retrieve(query: String, pages: Int): LiveData<Payload<List<Photo>>> {
        return object: BoundSource<List<Photo>, PicsListResponse>(executors) {
            override fun saveCallResult(item: PicsListResponse) {
                val picIds: List<Int> = item.pics.map { it.id }
                val picResult = QueryResult(query = query, picIds = picIds, total = item.total, next = item.nextPage)

                database.runInTransaction {
                    picData.insertPhotos(item.pics)
                    picData.insert(picResult)
                }
            }

            override fun shouldFetch(data: List<Photo>?): Boolean {
                return data == null || data.isEmpty() || listLimiter.shouldFetch(query)
            }

            override fun loadFromDb(): LiveData<List<Photo>> {
                return Transformations.switchMap(picData.search(query)) { data ->
                    if (data == null) {
                        EmptyLiveData.create()
                    } else {
                        picData.loadOrdered(data.picIds)
                    }
                }
            }

            override fun createCall(): LiveData<BaseResponse<PicsListResponse>> {
                return swiftlanePicService.getPicsByKeyword(Constants.API_KEY, query, pages)
            }
        }.asLiveData()
    }
}