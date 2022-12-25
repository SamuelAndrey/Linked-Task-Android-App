package com.samuel.linkedtask.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {

    @Update
    fun update(taskModel: TaskModel)

    @Delete
    fun delete(taskModel: TaskModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(taskModel: TaskModel)

    @Query("SELECT * FROM tableTask WHERE completed=:completed AND uid=:uid")
    fun taskAll(uid: String, completed: Boolean): LiveData<List<TaskModel>>

    @Query("SELECT * FROM tableTask WHERE completed=:completed AND date=:date AND uid=:uid")
    fun taskAll(uid: String, completed: Boolean, date: Long): LiveData<List<TaskModel>>

    @Query("DELETE FROM tableTask WHERE completed=1 AND uid=:uid")
    fun deleteCompleted(uid: String)

    @Query("DELETE FROM tableTask WHERE uid=:uid")
    fun deleteAll(uid: String)


}