package io.github.skywalkerdarren.simpleaccounting.adapter.diff

import androidx.recyclerview.widget.DiffUtil

class DefaultDiff<T> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem == newItem
    override fun areContentsTheSame(oldItem: T, newItem: T) = true
}