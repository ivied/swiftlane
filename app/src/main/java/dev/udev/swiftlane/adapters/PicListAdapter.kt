package dev.udev.swiftlane.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import dev.udev.swiftlane.R
import dev.udev.swiftlane.databinding.PhotoItemBinding
import dev.udev.swiftlane.infrastructure.BaseExecutors
import dev.udev.swiftlane.models.Photo

class PicListAdapter(appExecutors: BaseExecutors, private val photoClickCallback: ((Photo) -> Unit)?)
    : GenericListAdapter<Photo, PhotoItemBinding> (executors = appExecutors,
                                                   callback = object : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id && oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.imageUrl == newItem.imageUrl && oldItem.likesNumber == newItem.likesNumber
        }
    })
{
    override fun createBinding(parent: ViewGroup): PhotoItemBinding {
        val binding = DataBindingUtil.inflate<PhotoItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.photo_item,
            parent,
            false
        )
        binding.root.setOnClickListener {
            binding.photo?.let {
                photoClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: PhotoItemBinding, item: Photo) {
        binding.photo = item
    }
}