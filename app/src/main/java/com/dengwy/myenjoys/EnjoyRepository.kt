package com.dengwy.myenjoys

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class EnjoyRepository(application: Application) {
    private val CSVFILE: String = "allEnjoys.csv"
    private val UPDATEDFILE: String = "updated.txt"
    private val KEYFILEUPDATEDATETIME: String = "ENJOY_UPDATED_DATE"

    private val enjoyDao: EnjoyDao = EnjoyDatabase.getInstance(application.applicationContext).enjoyDao()
    private val enjoyInputStream: InputStream = application.resources.assets.open(CSVFILE)
    private val updatedInputStream: InputStream = application.resources.assets.open(UPDATEDFILE)
    val sp: SharedPreferences = application.getSharedPreferences("MY_ENJOY", Context.MODE_PRIVATE)

    fun findAllByType(type: String) : LiveData<List<Enjoy>> {
        return enjoyDao.findAllByType(type)
    }

    fun findByKeyword(keyword: String, type: String) : LiveData<List<Enjoy>> {
        return enjoyDao.findByKeyword(keyword, type)
    }

    fun initDB() {
        GlobalScope.launch {
            var lastModified: String = sp.getString(KEYFILEUPDATEDATETIME, "")!!
            var enjoyLastModified: String = getEnjoyFileLastModified()
            Log.d("EnjoyFragment", "sp modify date:$lastModified")
            Log.d("EnjoyFragment", "fileLastModified:$enjoyLastModified")
            // 只有文件更新过才再次加载
            if (enjoyLastModified > lastModified) {
                // 处理CSV文件，写DB
                var enjoyList: ArrayList<Enjoy> = arrayListOf()
                var inputStreamReader: InputStreamReader = InputStreamReader(enjoyInputStream)
                var bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                var line: String? = bufferedReader.readLine()
                while (line != null) {
                    line = bufferedReader.readLine()
                    line?.let {
                        parseEnjoy(it)?.let { enjoyList.add(it) }
                    }
                }
                deleteAll()
                insertAll(enjoyList)

                // 更新SharedPreference
                val spEdit = sp.edit()
                spEdit.putString(KEYFILEUPDATEDATETIME, enjoyLastModified)
                spEdit.apply()
            }
        }
    }

    private fun parseEnjoy(line: String) : Enjoy? {
        var enjoy: Enjoy? = null
        if (line != null && line.isNotEmpty()) {
            val slices: List<String> = line.split(",")
            if (slices.size == 5) {
                enjoy = newEnjoy(slices[0], slices[1], slices[2], slices[3], slices[4])
            }
        }
        return enjoy
    }

    private fun getEnjoyFileLastModified(): String {
        var inputStreamReader: InputStreamReader = InputStreamReader(updatedInputStream)
        var bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        return bufferedReader.readLine()
    }

    private suspend fun insertAll(enjoys: List<Enjoy>) {
        enjoyDao.insertAll(enjoys)
    }

    private suspend fun deleteAll() {
        enjoyDao.deleteAll()
    }
}