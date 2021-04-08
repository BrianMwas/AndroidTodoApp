package com.droid.koto.fragments

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.droid.koto.R
import com.droid.koto.data.models.Priority
import com.droid.koto.data.models.TodoData

class SharedViewModel(application: Application): AndroidViewModel(application) {

    private val _emptyTodos = MutableLiveData(true)
    val isEmpty : LiveData<Boolean>
        get() = _emptyTodos


    fun checkDbForTodos(todos: List<TodoData>) {
        _emptyTodos.value = todos.isEmpty()
    }


    val listener : AdapterView.OnItemSelectedListener = object: AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long,
        ) {
            Log.d("SharedView", "Parent ${parent?.getChildAt(0)}")
            when(position) {
                0 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.danger))
                1 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.warn))
                2 -> (parent?.getChildAt(0) as TextView).setTextColor(ContextCompat.getColor(application, R.color.green_500))
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
    fun validateData(title: String, description: String): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    fun parsePriority(priority: String): Priority {
        return when(priority) {
            "High priority" -> Priority.HIGH
            "Mid priority" -> Priority.MEDIUM
            "Low priority" -> Priority.LOW
            else -> Priority.LOW
        }
    }
}