package dev.udev.swiftlane.adapters

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import dev.udev.swiftlane.infrastructure.BaseExecutors

abstract class GenericListAdapter<T, V: ViewDataBinding>(executors: BaseExecutors,
                                                         callback: DiffUtil.ItemCallback<T>):
    ListAdapter<T, GenericViewHolder<V>>(AsyncDifferConfig.Builder<T>(callback)
        .setBackgroundThreadExecutor(executors.diskIO())
        .build()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<V> {
        val binding = createBinding(parent)
        return GenericViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: GenericViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position))
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T)
}