package dev.udev.swiftlane.infrastructure

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class BaseExecutors(private val diskIO: Executor,
                         private val networkIO: Executor,
                         private val mainUI: Executor) {

    @Inject
    constructor(): this(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        MainThreadExecutor())

    fun diskIO(): Executor {
        return diskIO
    }

    fun networkIO(): Executor {
        return networkIO
    }

    fun mainUI(): Executor {
        return mainUI
    }

    private class MainThreadExecutor: Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(task: Runnable) {
            mainThreadHandler.post(task)
        }
    }

}