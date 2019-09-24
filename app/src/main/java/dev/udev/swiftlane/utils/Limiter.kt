package dev.udev.swiftlane.utils

import android.os.SystemClock
import android.util.ArrayMap
import java.util.concurrent.TimeUnit

class Limiter<in K>(timeout: Int, unit: TimeUnit) {
    private val timestamps = ArrayMap<K, Long>()
    private val timeout = unit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFetch(key: K): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = SystemClock.uptimeMillis()

    @Synchronized
    fun reset(key: K) {
        timestamps.remove(key)
    }

}