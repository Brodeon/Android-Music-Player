package com.brodeon.musicplayer

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

class Song(val songURL: String, val artist: String, val duration: Long, val id: Long, val title: String): Parcelable, Serializable {

//    companion object {
//        @JvmField
//        val serialVersionUID = 1L
//
//        @JvmField
//        var CREATOR = object : Parcelable.Creator<Song>
//        {
//            override fun createFromParcel(source: Parcel?): Song {
//                return Song(source!!)
//            }
//
//            override fun newArray(size: Int): Array<Song> {
//                return newArray(size)
//            }
//        }
//    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(source: Parcel?): Song {
            return Song(source!!)
        }

        override fun newArray(size: Int): Array<Song> {
            return newArray(size)
        }

    }

    constructor(): this("", "", 0L, 0L, "")

    private constructor(inParcel: Parcel) : this(inParcel.readString()!!, inParcel.readString()!!, inParcel.readLong(), inParcel.readLong(), inParcel.readString()!!) {

    }

    override fun writeToParcel(outParcel: Parcel, flags: Int) {
        outParcel.writeString(songURL)
        outParcel.writeString(artist)
        outParcel.writeString(title)
        outParcel.writeLong(duration)
        outParcel.writeLong(id)

    }

    override fun describeContents(): Int {
        return 0
    }

}




