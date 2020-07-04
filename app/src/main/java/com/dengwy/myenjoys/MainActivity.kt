package com.dengwy.myenjoys

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewpager2.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = 5

            override fun createFragment(position: Int) = when(position) {
                0 -> EnjoyFragment.newInstance(TYPE_DRAMA)
                1 -> EnjoyFragment.newInstance(TYPE_MOVIE)
                2 -> EnjoyFragment.newInstance(TYPE_MORNING_DRAMA)
                3 -> EnjoyFragment.newInstance(TYPE_SPECIAL)
                else -> EnjoyFragment.newInstance(TYPE_ANIMATION)
            }
        }

        TabLayoutMediator(tablayout, viewpager2){tab, position ->
            when(position) {
                0 -> tab.text = TYPE_DRAMA
                1 -> tab.text = TYPE_MOVIE
                2 -> tab.text = TYPE_MORNING_DRAMA
                3 -> tab.text = TYPE_SPECIAL
                else -> tab.text = TYPE_ANIMATION
            }
        }.attach()

        tablayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ff0000"))

        val enjoyRepository = EnjoyRepository(application)
        enjoyRepository.initDB()


    }
}