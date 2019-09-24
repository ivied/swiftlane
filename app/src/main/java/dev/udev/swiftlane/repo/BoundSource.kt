package dev.udev.swiftlane.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import dev.udev.swiftlane.infrastructure.BaseExecutors
import dev.udev.swiftlane.models.Payload
import dev.udev.swiftlane.network.responses.BaseResponse
import dev.udev.swiftlane.network.responses.EmptyResponse
import dev.udev.swiftlane.network.responses.ErrorResponse
import dev.udev.swiftlane.network.responses.SuccessResponse

abstract class BoundSource<CacheType, NetworkType>
@MainThread constructor(private val appExecutors: BaseExecutors) {
    private val result = MediatorLiveData<Payload<CacheType>>()

    init {
        result.value = Payload.inProgress(null)

        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data: CacheType ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData: CacheType ->
                    setValue(Payload.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Payload<CacheType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<CacheType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData: CacheType ->
            setValue(Payload.inProgress(newData))
        }
        result.addSource(apiResponse) { response: BaseResponse<NetworkType> ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is SuccessResponse -> {
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                        appExecutors.mainUI().execute {
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Payload.success(newData))
                            }
                        }
                    }
                }
                is EmptyResponse -> {
                    appExecutors.mainUI().execute {
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Payload.success(newData))
                        }
                    }
                }
                is ErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Payload.error(response.errorMsg, newData))
                    }
                }
            }
        }
    }

    @WorkerThread
    protected open fun processResponse(response: SuccessResponse<NetworkType>): NetworkType {
        return response.body
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: NetworkType)

    @MainThread
    protected abstract fun shouldFetch(data: CacheType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<CacheType>

    @MainThread
    protected abstract fun createCall(): LiveData<BaseResponse<NetworkType>>

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Payload<CacheType>>
}