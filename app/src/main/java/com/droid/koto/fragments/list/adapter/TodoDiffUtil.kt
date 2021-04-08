package com.droid.koto.fragments.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.droid.koto.data.models.TodoData

class TodoDiffUtil(
        private val  oldData: List<TodoData>,
        private val newData: List<TodoData>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition] == newData[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldData[oldItemPosition].id == newData[newItemPosition].id &&
                oldData[oldItemPosition].title == newData[newItemPosition].title &&
                oldData[oldItemPosition].description == newData[newItemPosition].description &&
                oldData[oldItemPosition].priority == newData[newItemPosition].priority
    }

}