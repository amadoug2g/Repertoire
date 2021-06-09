package com.playgroundagc.songtracker.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.playgroundagc.songtracker.model.Song
import com.playgroundagc.songtracker.databinding.SongCardviewBinding

/**
 * Created by Amadou on 08/06/2021, 22:10
 *
 * RecyclerView Adapter File
 *
 */

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var songList = emptyList<Song>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = songList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return songList.size
    }

    fun setData(song: List<Song>) {
        this.songList = song
        notifyDataSetChanged()
    }

    class MyViewHolder(private val binding: SongCardviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            with(binding) {
                binding.song = song
            }
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    SongCardviewBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }
}