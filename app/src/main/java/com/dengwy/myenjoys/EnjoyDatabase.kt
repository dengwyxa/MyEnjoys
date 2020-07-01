package com.dengwy.myenjoys

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Enjoy::class), version = 1, exportSchema = false)
abstract class EnjoyDatabase : RoomDatabase() {
    abstract fun enjoyDao(): EnjoyDao

    companion object{
        private var instance : EnjoyDatabase? = null
        fun getInstance(context: Context) : EnjoyDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context,
                    EnjoyDatabase::class.java,
                    "enjoys.db"
                ).build()
            }
            return instance as EnjoyDatabase
        }
    }
}