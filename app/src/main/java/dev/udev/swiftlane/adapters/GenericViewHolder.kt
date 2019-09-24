package dev.udev.swiftlane.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class GenericViewHolder<out T: ViewDataBinding> constructor(val binding:T): RecyclerView.ViewHolder(binding.root)