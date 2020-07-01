package com.dengwy.myenjoys

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface EnjoyDao {
    @Query("SELECT * FROM Enjoy WHERE type = :type ORDER BY year DESC")
    fun findAllByType(type: String) : LiveData<List<Enjoy>>

    @Query("SELECT * FROM Enjoy WHERE type = :type AND ((title LIKE :keyword) OR (actors LIKE :keyword) OR (year LIKE :keyword)) ORDER BY year DESC")
    fun findByKeyword(keyword: String, type: String) : LiveData<List<Enjoy>>

    @Insert
    fun insertAll(enjoys: List<Enjoy>)

    @Query("DELETE FROM Enjoy")
    fun deleteAll()
}