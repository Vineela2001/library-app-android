package com.example.libraryapp.room

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// step 1 : create db as abstract  class with proper annotation and it extends RoomDatabase
@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookDB : RoomDatabase() {

    // step2 define fun to instantiate DAO
    abstract fun bookDao(): BookDAO

    // Step3 - define companion object
    companion object {
        @Volatile // to allow the instance accessible for all threads
        private var INSTANCE: BookDB? = null


        fun getInstance(context: Context): BookDB {
            synchronized(this) { // one thread can enter at a time
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BookDB::class.java,
                        "books_db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}