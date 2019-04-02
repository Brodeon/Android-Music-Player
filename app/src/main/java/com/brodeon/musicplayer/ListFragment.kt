package com.brodeon.musicplayer

import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*
import kotlin.collections.ArrayList


class ListFragment : Fragment() {

    lateinit var adapter: MainListRecycleViewAdapter
    var songsList = ArrayList<Song>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        retrieveSongs()
        adapter.updateList(songsList)
    }

    private fun setupRecycleView() {
        val list = main_list
        list.layoutManager = LinearLayoutManager(context)
        adapter = MainListRecycleViewAdapter(activity!!)
        list.adapter = adapter
    }

    private fun retrieveSongs() {
        songsList = ArrayList<Song>()
        val projections = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA)

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        var cursor = activity!!.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projections, selection, null, null)
        while (cursor!!.moveToNext()) {
            val id = cursor.getString(0)
            val album = cursor.getString(1)
            val artist = cursor.getString(2)
            val duration = cursor.getString(3)
            val title = cursor.getString(4)
            val data = cursor.getString(5)

            val song = Song(data, artist, duration.toLong(), id.toLong(), title)
            songsList.add(song)
        }


        cursor = activity!!.contentResolver.query(MediaStore.Audio.Media.INTERNAL_CONTENT_URI, projections, selection, null, null)
        while (cursor!!.moveToNext()) {
            val id = cursor.getString(0)
            val album = cursor.getString(1)
            val artist = cursor.getString(2)
            val duration = cursor.getString(3)
            val title = cursor.getString(4)
            val data = cursor.getString(5)

            val song = Song(data, artist, duration.toLong(), id.toLong(), title)
            songsList.add(song)
        }

        cursor.close()

        Collections.sort(songsList) { o1, o2 ->
            val artist1 = o1.artist
            val artist2 = o2.artist
            val comparedArtists = artist1.compareTo(artist2)

            if (comparedArtists == 0) {
                o1.title.compareTo(o2.title)
            } else {
                comparedArtists
            }
        }

        songsList = songsList.filter { it.duration > 15000 } as ArrayList<Song>
    }

}
