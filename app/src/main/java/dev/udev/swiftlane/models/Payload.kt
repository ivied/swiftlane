package dev.udev.swiftlane.models

data class Payload<out T>(val state: State, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Payload<T> {
            return Payload(State.Success, data, null)
        }

        fun <T> error(msg: String, data: T?): Payload<T> {
            return Payload(State.Error, data, msg)
        }

        fun <T> inProgress(data: T?): Payload<T> {
            return Payload(State.Pending, data, null)
        }
    }
}