package com.dengwy.myenjoys

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Enjoy(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "year") val year: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "season") val season: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "actors") val actors: String
)

fun newEnjoy(year: String, type: String, season: String, title: String, actors: String): Enjoy {
    return Enjoy(0, year, type, season, title, actors)
}