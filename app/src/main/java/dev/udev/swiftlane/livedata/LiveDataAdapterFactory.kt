package dev.udev.swiftlane.livedata

import androidx.lifecycle.LiveData
import dev.udev.swiftlane.network.responses.BaseResponse
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataAdapterFactory: CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>, retrofit: Retrofit): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) return null

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        val rawObservableType = getRawType(observableType)
        if (rawObservableType != BaseResponse::class.java)
            throw IllegalArgumentException("Payload must be of BaseResponse type")

        if (observableType !is ParameterizedType)
            throw IllegalArgumentException("Payload must be parameterized")

        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataAdapter<Any>(bodyType)
    }
}