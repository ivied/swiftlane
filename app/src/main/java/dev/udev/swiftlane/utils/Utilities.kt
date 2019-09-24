package dev.udev.swiftlane.utils

import android.annotation.SuppressLint
import android.text.TextUtils
import java.util.*

object Utilities {
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun tags(from: String?): String {
        return when {
            from == null -> ""
            from.contains(",") -> TextUtils.join(" - ", from.toUpperCase(Locale.getDefault()).split(", "))
            else -> from
        }
    }

    @JvmStatic
    fun by(user: String): String {
        return "by: $user"
    }

    @JvmStatic
    fun likes(count: Int): String {
        return  "(with $count likes)"
    }
}