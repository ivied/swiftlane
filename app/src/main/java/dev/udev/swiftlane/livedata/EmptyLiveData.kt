package dev.udev.swiftlane.livedata

import androidx.lifecycle.LiveData

class EmptyLiveData<T: Any?> private constructor(): LiveData<T>() {
    init {
        postValue(null)
    }

    companion object {
        fun <T> create(): LiveData<T> {
            return EmptyLiveData()
        }
    }
}