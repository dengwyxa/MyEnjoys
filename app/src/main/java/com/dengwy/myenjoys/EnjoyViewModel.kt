package com.dengwy.myenjoys

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class EnjoyViewModel(typeParam: String, application: Application) : AndroidViewModel(application) {
    private val type = typeParam
    private val enjoyRepository = EnjoyRepository(application)

    fun findAllByType(): LiveData<List<Enjoy>> {
        return enjoyRepository.findAllByType(type)
    }

    fun findByKeyword(keyword: String) : LiveData<List<Enjoy>> {
        return enjoyRepository.findByKeyword("%$keyword%", type)
    }

    class Factory constructor(private val type: String, private val application: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return EnjoyViewModel(type, application) as T
        }
    }
}