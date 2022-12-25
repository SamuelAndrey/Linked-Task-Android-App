package com.samuel.linkedtask.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [TaskModel::class],
    exportSchema = false,
    version = 1
)
abstract class DatabaseService: RoomDatabase() {
    abstract fun taskDao(): TaskDao

}