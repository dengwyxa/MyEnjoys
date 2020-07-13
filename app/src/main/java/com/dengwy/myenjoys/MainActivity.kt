package com.dengwy.myenjoys

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
                0 -> {
                    tab.text = TYPE_DRAMA
                    tab.setIcon(R.drawable.ic_baseline_live_tv_24)
                }
                1 -> {
                    tab.text = TYPE_MOVIE
                    tab.setIcon(R.drawable.ic_baseline_movie_24)
                }
                2 -> {
                    tab.text = TYPE_MORNING_DRAMA
                    tab.setIcon(R.drawable.ic_baseline_wb_morning_24)
                }
                3 -> {
                    tab.text = TYPE_SPECIAL
                    tab.setIcon(R.drawable.ic_baseline_tvsp_24)
                }
                else -> {
                    tab.text = TYPE_ANIMATION
                    tab.setIcon(R.drawable.ic_baseline_ani_24)
                }
            }
        }.attach()

        val enjoyRepository = EnjoyRepository(application)
        enjoyRepository.initDB()
    }
}