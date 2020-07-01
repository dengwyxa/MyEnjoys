package com.dengwy.myenjoys

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EnjoyRepository(application: Application) {
    val enjoyDao: EnjoyDao = EnjoyDatabase.getInstance(application.applicationContext).enjoyDao()

    fun findAllByType(type: String) : LiveData<List<Enjoy>> {
        return enjoyDao.findAllByType(type)
    }

    fun findByKeyword(keyword: String, type: String) : LiveData<List<Enjoy>> {
        return enjoyDao.findByKeyword(keyword, type)
    }

    fun initDB() {
        GlobalScope.launch {
            val drama = newEnjoy("2019", "日剧", "秋", "G弦上的你和我", "波瑠 中川大志")
            val movie = newEnjoy("2011", "日影", "-", "白夜行", "堀北真希 高良健吾")
            val morning = newEnjoy("2003", "晨间剧", "秋", "晴朗家族", "上野树里 石原里美 锦户亮")
            val sp = newEnjoy("2019", "SP", "-", "下町火箭", "阿部宽 竹内凉真")
            val animation = newEnjoy("2016", "动画", "-", "你的名字", "不明")
            val drama2 = newEnjoy("2013", "日剧", "夏", "半泽直树", "堺雅人 香川照之 及川光博 泷藤贤一 上户彩")
            val movie2 = newEnjoy("2016", "日影", "-", "垫底辣妹", "有村架纯 伊藤淳史")
            val morning2 = newEnjoy("2013", "晨间剧", "春", "海女", "能年玲奈 桥本爱 小泉今日子 福士苍汰 药师丸博子 小池彻平")
            val sp2 = newEnjoy("2018", "SP", "-", "女儿的结婚", "中井贵一 波瑠")
            val animation2 = newEnjoy("2001", "动画", "-", "千与千寻", "不明")
            deleteAll()
            insertAll(listOf(drama, movie, morning, sp, animation, drama2, movie2, morning2, sp2, animation2))
        }
    }

    private suspend fun insertAll(enjoys: List<Enjoy>) {
        enjoyDao.insertAll(enjoys)
    }

    private suspend fun deleteAll() {
        enjoyDao.deleteAll()
    }
}