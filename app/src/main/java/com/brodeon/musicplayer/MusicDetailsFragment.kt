package com.brodeon.musicplayer

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_music_details.*


class MusicDetailsFragment : Fragment() {
    val TAG: String = "MusicFragment"

    private lateinit var seekBar: SeekBar
    private lateinit var playPauseButton: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var artistTextView: TextView

    var shouldAdvancingSeekBar: Boolean = true
    val handler = Handler()
    var mainActivity: MainActivity? = null

    val seekThread: Runnable = object: Runnable {
        override fun run() {
            if (mainActivity?.songService?.isPlaying!! && shouldAdvancingSeekBar) {
                updateSeekBarProgress()
            }
            handler.postDelayed(this, 100)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity?.hidePlaybar()

        titleTextView = title_textview
        artistTextView = artist_textview

        titleTextView.text = mainActivity?.songService?.songTitle
        artistTextView.text = mainActivity?.songService?.songArtist

        playPauseButton = play_pause_button
        seekBar = seek_bar
        seekBar.max = mainActivity?.songService?.duration!!
        updateSeekBarProgress()
        seekBar.setOnSeekBarChangeListener(seekBarChangeListener)

        playPauseButton.setOnClickListener {
            mainActivity?.songService?.playPause()
        }

        handler.post(seekThread)

    }

    val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            shouldAdvancingSeekBar = false
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            shouldAdvancingSeekBar = true
            mainActivity?.songService?.setNewPosition(seekBar!!.progress)
        }
    }



    fun updateSeekBarProgress() {
        seekBar.progress = mainActivity?.songService?.currentPosition!!
    }

    override fun onDetach() {
        handler.removeCallbacks(seekThread)
        mainActivity?.setArtistAndTitleFromService()
        super.onDetach()
    }
}
