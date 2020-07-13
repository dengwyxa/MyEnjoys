package com.dengwy.myenjoys

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class EnjoyAdapter(enjoyViewModelParam: EnjoyViewModel) : ListAdapter<Enjoy, MyViewHolder>(DIFFCALLBACK) {
    var enjoyViewModel: EnjoyViewModel = enjoyViewModelParam

    object DIFFCALLBACK : DiffUtil.ItemCallback<Enjoy>() {
        override fun areItemsTheSame(oldItem: Enjoy, newItem: Enjoy): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Enjoy, newItem: Enjoy): Boolean {
            return oldItem.uid == newItem.uid
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val holder = MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.enjoy_cell, parent, false))
        holder.itemView.setOnClickListener{
            val uri =
                Uri.parse("https://baike.baidu.com/item/" + holder.title.text)
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri
            holder.itemView.context.startActivity(intent)
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val enjoy : Enjoy = getItem(position)

        holder.apply {
            yearSeason.text = if (enjoy.season.equals("-")) enjoy.year else enjoy.year + enjoy.season
            title.text = enjoy.title
            actors.text = enjoy.actors
        }
    }
}

class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
    var yearSeason: TextView = itemView.findViewById(R.id.yearSeason)
    var title: TextView = itemView.findViewById(R.id.title)
    var actors: TextView = itemView.findViewById(R.id.actors)
}