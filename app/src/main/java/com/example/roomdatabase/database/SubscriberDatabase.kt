package com.example.roomdatabase.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase : RoomDatabase() {

    //NOTE in this small project we only have one entity class and corresponding
    // DAO interface if we had more entity classes  we would have listed all here
    // and defined the corresponding DAO's here


    abstract val subscriberDAO : SubscriberDAO

    //NOTE bu kod hissəsi bütün databaselərdə eyni olur, sadəcə class
    // və database namei dəyişmək lazım olur


    companion object{
        @Volatile
        private var INSTANCE : SubscriberDatabase? = null
        fun getInstance(context: Context): SubscriberDatabase{
            synchronized(this){
                var instance = INSTANCE
                 if(instance==null){
                    instance = Room.databaseBuilder(
                         context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_database"
                    ).build()
                     INSTANCE=instance
                }
                return  instance
            }
        }
    }
}