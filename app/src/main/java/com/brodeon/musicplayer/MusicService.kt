package com.brodeon.musicplayer

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import android.os.Process
import java.lang.Exception
import java.net.URI

class MusicService: Service() {

    val player: MediaPlayer = MediaPlayer()

    val binder = object: ISongService.Stub() {
        override fun getPid(): Int = Process.myPid()
        override fun getSongTitle(): String = title!!
        override fun getSongArtist(): String = artist!!
        override fun isPlaying(): Boolean = player.isPlaying

        override fun playPause() {
            if (player.isPlaying) {
                player.pause()
            } else {
                player.start()
            }
        }

        override fun playSong(uri: String?) {
            playSongOnInitialize(null, uri)
        }
    }

    private var title: String? = null
    private var artist: String? = null

    override fun onBind(intent: Intent?): IBinder? {
        playSongOnInitialize(intent)
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        playSongOnInitialize(intent)
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        player.setAudioAttributes(AudioAttributes.Builder().setFlags(AudioAttributes.CONTENT_TYPE_MUSIC).build())
    }

    fun playSongOnInitialize(intent: Intent?, customUri: String? = null) {
        var uri: Uri? = null
        if (intent != null) {
            val bundle = intent.extras
            uri = Uri.parse(bundle?.getString("uri"))
            title = bundle?.getString("title")
            artist = bundle?.getString("artist")
        } else if (customUri != null) {
            uri = Uri.parse(customUri)
        }

        try {
            player.stop()
            player.reset()
            player.setDataSource(this, uri)
            player.prepare()
            player.start()
        } catch (e: Exception) {

        }
    }
}