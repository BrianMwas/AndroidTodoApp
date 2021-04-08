package com.droid.koto.data.repository

import androidx.lifecycle.LiveData
import com.droid.koto.data.models.TodoData

class TodoRepository(private val todoDao: ITodoDao) {
    val getData: LiveData<List<TodoData>> = todoDao.getAllTodos()
    val sortByHighPriority = todoDao.sortByHighPriority()
    val sortByLowPriority = todoDao.sortByLowPriority()

    suspend fun createTodo(todoData: TodoData) = todoDao.create(todoData)

    suspend fun updateTodo(todoData: TodoData) = todoDao.updateTodo(todoData)

    suspend fun deleteTodo(todo: TodoData) = todoDao.deleteTodo(todo)

    suspend fun deleteAll() = todoDao.deleteAll()

    fun searchTodo(q: String): LiveData<List<TodoData>> = todoDao.searchTodos(q)
}