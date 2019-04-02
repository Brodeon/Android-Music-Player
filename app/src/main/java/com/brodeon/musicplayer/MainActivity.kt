package com.brodeon.musicplayer

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.preference.PreferenceManager
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.brodeon.musicplayer.R.id.to_music_details

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var songService: ISongService? = null
    private lateinit var playBar: View
    private lateinit var playPauseBtn: ImageView
    private lateinit var playBarTextView: TextView

    private var isConnected: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findViewById<View>(R.id.nav_host_fragment)

        playBar = play_bar
        playBar.setOnClickListener {
            Navigation.findNavController(navController).navigate(R.id.musicDetailsFragment)
        }
        playBar.visibility = View.GONE

        playPauseBtn = play_pause_btn
        playPauseBtn.setOnClickListener {
            songService?.playPause()
        }

        playBarTextView = player_bar_tv
    }

    override fun onResume() {
        super.onResume()
        checkIsPlayingAndChangeImage()
    }

    fun startSong(intent: Intent, uri: String) {
        if (isConnected) {
            songService?.playSong(uri, intent.extras.getString("artist"), intent.extras.getString("title"))
            changePlayerBarTextAndImage()
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

    fun hidePlaybar() {
        playBar.visibility = View.GONE
    }

    override fun onDestroy() {
        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        super.onDestroy()
    }


    fun checkIsPlayingAndChangeImage() {
        songService?.isPlaying?.let {isPlaying ->
            if (isPlaying) {
                playPauseBtn.setImageResource(R.drawable.pause)
            } else {
                playPauseBtn.setImageResource(android.R.drawable.ic_media_play)
            }
        }
    }

    fun changePlayerBarTextAndImage() {
        playBarTextView.text = "${songService?.songArtist} - ${songService?.songTitle}"
    }
}
