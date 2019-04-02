// ISongService.aidl
package com.brodeon.musicplayer;

// Declare any non-default types here with import statements

interface ISongService {

    int getPid();
    String getSongTitle();
    String getSongArtist();
    boolean isPlaying();
    void playPause();
    void playSong(String url, String artist, String title);
    int getDuration();
    int getCurrentPosition();
    void setNewPosition(int position);

}
