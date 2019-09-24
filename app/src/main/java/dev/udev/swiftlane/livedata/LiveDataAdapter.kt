package dev.udev.swiftlane.livedata

import androidx.lifecycle.LiveData
import dev.udev.swiftlane.network.responses.BaseResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataAdapter<T>(private val responseType: Type): CallAdapter<T, LiveData<BaseResponse<T>>> {
    override fun responseType() = responseType
    override fun adapt(call: Call<T>): LiveData<BaseResponse<T>> {
        return object : LiveData<BaseResponse<T>>() {
            private var alreadyOn = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (alreadyOn.compareAndSet(false, true)) {
                    call.enqueue(object  : Callback<T> {
                        override fun onFailure(call: Call<T>, t: Throwable) {
                            postValue(BaseResponse.create(t))
                        }

                        override fun onResponse(call: Call<T>, response: Response<T>) {
                            postValue(BaseResponse.create(response))
                        }

                    })
                }
            }
        }
    }
}