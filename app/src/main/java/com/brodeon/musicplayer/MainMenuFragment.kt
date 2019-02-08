package com.brodeon.musicplayer


import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.Navigation
import com.brodeon.musicplayer.R.id.to_list_fragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_main_menu.*

class MainMenuFragment : Fragment() {

    private val PERMISSION_READ_EXT_STORAGE: Int = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }

    private fun setButtonsListeners() {
        val tracksButton = tracks_button
        tracksButton.setOnClickListener {
            Navigation.findNavController(it).navigate(to_list_fragment)
        }

        val artistsButton = artists_button
        artistsButton.setOnClickListener {
            Navigation.findNavController(it).navigate(to_list_fragment)
        }

        val albumsButton = albums_button
        albumsButton.setOnClickListener {
            Navigation.findNavController(it).navigate(to_list_fragment)
        }
    }

    private fun requestPermissions() {
        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity as AppCompatActivity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                val snackbar = Snackbar.make(activity!!.findViewById(android.R.id.content), "You have to grant the permission", Snackbar.LENGTH_LONG)
                snackbar.setAction("Request") {
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_EXT_STORAGE)
                }
            } else {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), PERMISSION_READ_EXT_STORAGE)
            }
        } else {
            setButtonsListeners()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            PERMISSION_READ_EXT_STORAGE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setButtonsListeners()
                }
            }
        }
    }
}
