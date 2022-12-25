package com.samuel.linkedtask.database

import android.content.Context
import androidx.room.Room

private const val dbName = "LinkedTask2.db"

object DatabaseClient {
    fun getService(context: Context): DatabaseService {
        return Room.databaseBuilder(
            context,
            DatabaseService::class.java,
            dbName
        ).build()
    }
}