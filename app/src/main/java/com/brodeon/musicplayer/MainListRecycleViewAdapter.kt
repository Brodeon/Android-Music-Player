package com.brodeon.musicplayer

import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item_layout.view.*

class MainListRecycleViewAdapter(var context: Context): RecyclerView.Adapter<MainListRecycleViewAdapter.MainListViewHolder>()  {

    var songsList: List<Song>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_layout, parent, false)
        return MainListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songsList?.size ?: 0
    }

    override fun onBindViewHolder(holder: MainListViewHolder, position: Int) {
        songsList?.let {
            val song = it[position]
            holder.songLabel.text = "${song.artist} - ${song.title}"

            val url = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, song.id)
            holder.view.setOnClickListener {
                val mainActivity = context as MainActivity
                Intent(context, MusicService::class.java).also { intent ->
                    intent.putExtra("uri", url.toString())
                    intent.putExtra("title", song.title)
                    intent.putExtra("artist", song.artist)
                    mainActivity.startSong(intent, song.songURL)
                }
            }
        }
    }

    fun updateList(songs: List<Song>) {
        songsList = songs
        notifyDataSetChanged()
    }

    inner class MainListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var view = itemView
        var songImage: ImageView = itemView.song_image
        var songLabel: TextView = itemView.title_tv
    }
}