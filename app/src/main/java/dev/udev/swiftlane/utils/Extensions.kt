package dev.udev.swiftlane.utils

import androidx.fragment.app.Fragment
import dev.udev.swiftlane.lifecycle.ClearableData

fun <T: Any> Fragment.clearable() = ClearableData<T>(this)