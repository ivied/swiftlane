package dev.udev.swiftlane.ui.binding

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import dev.udev.swiftlane.R

object Adapters {
    @JvmStatic
    @BindingAdapter("visibleGone")
    fun showHide(view: View, show: Boolean) {
        view.visibility = if (show) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun showPhoto (imageView: ImageView, url : String) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.white_background)
            .into(imageView)
    }
}