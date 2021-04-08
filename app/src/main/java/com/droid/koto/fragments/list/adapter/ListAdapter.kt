package com.droid.koto.fragments.list.adapter

import android.util.Log
import android.view.LayoutInflater

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.droid.koto.R
import com.droid.koto.data.models.Priority
import com.droid.koto.data.models.TodoData
import com.droid.koto.databinding.RowLayoutBinding
import com.droid.koto.fragments.list.ListFragmentDirections

class ListAdapter: RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    var dataList = emptyList<TodoData>()

    class ListViewHolder(private val rowBinding: RowLayoutBinding): RecyclerView.ViewHolder(rowBinding.root) {
        fun bind(todoData: TodoData) {
            rowBinding.titleTv.text = todoData.title
            rowBinding.rowDescriptionTv.text = todoData.description
            rowBinding.rowBackground.setOnClickListener {
                val actions = ListFragmentDirections.actionListFragmentToUpdateFragment(todoData)
                rowBinding.root.findNavController().navigate(actions)
            }

            when(todoData.priority) {
                Priority.HIGH -> {
                    rowBinding.priorityIndicator.setImageDrawable(
                        ContextCompat.getDrawable(
                            rowBinding.root.context,
                            R.drawable.high_priority
                        )
                    )
                }
                Priority.MEDIUM -> {

                    rowBinding.priorityIndicator.setImageDrawable(
                        ContextCompat.getDrawable(
                            rowBinding.root.context,
                            R.drawable.mid_priority
                        )
                    )
                }
                Priority.LOW -> {
                    Log.d("Adapter 3", todoData.priority.toString())
                    rowBinding.priorityIndicator.setImageDrawable(
                        ContextCompat.getDrawable(
                            rowBinding.root.context,
                            R.drawable.low_priority
                        )
                    )
                }
            }

        }

        companion object {
            fun from(parent: ViewGroup) : ListViewHolder {
                val view = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(view, parent, false)
                return ListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val currentTodo = dataList[position]
        holder.bind(currentTodo)
    }

    override fun getItemCount(): Int = dataList.size

    fun setData(data: List<TodoData>) {
        val todoDiffUtil = TodoDiffUtil(dataList, data)
        val result = DiffUtil.calculateDiff(todoDiffUtil)
        this.dataList = data
        result.dispatchUpdatesTo(this)
    }
}