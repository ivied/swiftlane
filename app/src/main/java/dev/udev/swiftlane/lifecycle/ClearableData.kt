package dev.udev.swiftlane.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class ClearableData<T: Any>(val fragment: Fragment): ReadWriteProperty<Fragment, T> {
    private var data: T? = null

    init {
        fragment.lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroy() {
                data = null
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return data ?: throw IllegalStateException("You should never try to get it when it is not available.")
    }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        data = value
    }
}