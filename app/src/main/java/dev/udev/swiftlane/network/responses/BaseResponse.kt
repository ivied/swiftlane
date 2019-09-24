package dev.udev.swiftlane.network.responses

import retrofit2.Response

sealed class BaseResponse<T> {
    companion object {
        fun <T> create(response: Response<T>): BaseResponse<T> {
            return when {
                response.isSuccessful -> {
                    val body = response.body()
                    if (body == null || response.code() == 204) EmptyResponse() else SuccessResponse(body)
                }
                else -> {
                    val msg = response.errorBody()?.toString()
                    if (msg.isNullOrEmpty()) ErrorResponse(response.message()) else ErrorResponse(msg)
                }
            }
        }

        fun <T> create(error: Throwable): ErrorResponse<T> {
            return ErrorResponse(error.message ?: "Undefined")
        }
    }
}

data class SuccessResponse<T>(val body: T): BaseResponse<T>()
data class ErrorResponse<T>(val errorMsg: String): BaseResponse<T>()

class EmptyResponse<T>: BaseResponse<T>()