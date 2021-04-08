package com.droid.koto.data.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.droid.koto.data.models.TodoData

@Dao
interface ITodoDao {

    @Query("SELECT * FROM todo_table ORDER BY id ASC")
    fun getAllTodos() : LiveData<List<TodoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun create(todo: TodoData)

    @Update
    suspend fun updateTodo(todo: TodoData)

    @Delete
    suspend fun deleteTodo(todo: TodoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM todo_table WHERE title LIKE :query")
    fun searchTodos(query: String): LiveData<List<TodoData>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'H%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority() : LiveData<List<TodoData>>

    @Query("SELECT * FROM todo_table ORDER BY CASE WHEN priority LIKE 'L%' THEN 1 WHEN priority LIKE 'M%' THEN 2 WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority() : LiveData<List<TodoData>>
}