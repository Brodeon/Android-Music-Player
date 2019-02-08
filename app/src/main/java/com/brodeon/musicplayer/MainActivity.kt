package com.brodeon.musicplayer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var songService: ISongService? = null
    private lateinit var playBar: View
    private lateinit var playPauseBtn: ImageButton
    private lateinit var playBarTextView: TextView

    private var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playBar = play_bar
        playBar.visibility = View.GONE
        playPauseBtn = play_pause_btn
        playPauseBtn.setOnClickListener {
            songService?.playPause()
        }
        playBarTextView = player_bar_tv
    }

    fun startSong(intent: Intent, uri: String) {
        if (isConnected) {
            songService?.playSong(uri)
        } else {
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    private val connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            songService = null
            isConnected = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            songService = ISongService.Stub.asInterface(service)
            isConnected = true
            setArtistAndTitleFromService()
        }
    }

    fun setArtistAndTitleFromService() {
        playBar.visibility = View.VISIBLE
        playBarTextView.text = "${songService?.songArtist} - ${songService?.songTitle}"
    }
}
