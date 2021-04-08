package com.droid.koto.data.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.droid.koto.data.TodoDatabase
import com.droid.koto.data.models.TodoData
import com.droid.koto.data.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application): AndroidViewModel(application) {
    private val todoDAO = TodoDatabase.getDatabase(application).todoDAO()
    private val todoRepo : TodoRepository = TodoRepository(todoDAO)

    fun getTodos() : LiveData<List<TodoData>> = todoRepo.getData

    fun insertData(todoData: TodoData) = viewModelScope.launch(Dispatchers.IO) { todoRepo.createTodo(todoData) }

    fun updateData(todoData: TodoData) = viewModelScope.launch(Dispatchers.IO) { todoRepo.updateTodo(todoData) }

    fun deleteData(todoData: TodoData) = viewModelScope.launch(Dispatchers.IO) { todoRepo.deleteTodo(todoData) }

    fun deleteEverything() = viewModelScope.launch(Dispatchers.IO) { todoRepo.deleteAll() }

    fun searchTasks(q: String): LiveData<List<TodoData>> = todoRepo.searchTodo(q)

    fun sortByHighPriority(): LiveData<List<TodoData>> = todoRepo.sortByHighPriority

    fun sortByLowPriority(): LiveData<List<TodoData>> = todoRepo.sortByLowPriority
}
