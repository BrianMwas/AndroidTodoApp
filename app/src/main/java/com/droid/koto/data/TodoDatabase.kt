package com.droid.koto.data

import android.content.Context
import androidx.room.*
import com.droid.koto.data.models.TodoData
import com.droid.koto.data.repository.ITodoDao

@Database(entities = [TodoData::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDAO(): ITodoDao

    companion object {
        @Volatile
        private var INSTANCE: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            val temp = INSTANCE
            if (temp != null) {
                return temp
            }
            synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        TodoDatabase::class.java,
                        "todo_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}