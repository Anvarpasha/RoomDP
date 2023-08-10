package com.example.roomdatabase.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface SubscriberDAO {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber) : Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber) : Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber): Int

    @Query("DELETE FROM subscriber_data_table")
    suspend fun deleteAll()  : Int

    //NOTE 2.1  burda bütün subscribersləri LiveData olaraq döndürən funksiya var
    @Query("SELECT * FROM subscriber_data_table")
    fun getAllSubscribers() : LiveData<List<Subscriber>>
}