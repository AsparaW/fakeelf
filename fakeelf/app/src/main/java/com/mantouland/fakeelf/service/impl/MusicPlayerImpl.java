package com.mantouland.fakeelf.service.impl;

import android.media.MediaPlayer;
import android.util.Log;

import com.mantouland.fakeelf.controller.MusicController;

import java.io.IOException;

/**
 * Created by asparaw on 2019/4/15.
 */
public class MusicPlayerImpl implements com.mantouland.fakeelf.service.MediaPlayer {
    private MediaPlayer mediaPlayer;
    private static final String TAG = "mt player ";
    private int totlength;

    @Override
    public void play(String path) {
/*        if (mediaPlayer!=null)
            stop();*/
        Log.d(TAG, "play: "+path);
        mediaPlayer= new MediaPlayer();
        path=path.trim();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                totlength=mediaPlayer.getDuration();
                mediaPlayer.start();
            });
            mediaPlayer.setOnCompletionListener(mp -> {
                mp.stop();
                mp.release();
                MusicController.getInstance().playRandom();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }else {
            mediaPlayer.start();
        }
    }

    @Override
    public void stop() {

        if (mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer=null;
        }
    }

    @Override
    public int getTime() {
        if (isPlaying())
            return mediaPlayer.getCurrentPosition();
        else
            return -2;
    }

    @Override
    public void seek(int time) {
        mediaPlayer.seekTo(time);
    }

    @Override
    public boolean isPlaying() {
        if (mediaPlayer==null)
            return false;
        boolean isPlaying=false;
        try {
            isPlaying = mediaPlayer.isPlaying();
        }
        catch (IllegalStateException e) {
            //mediaPlayer = null;
            //mediaPlayer = new MediaPlayer();
        }
        return isPlaying;
    }

    @Override
    public void setLoop(boolean isLoop) {
        if (mediaPlayer!=null){
            mediaPlayer.setLooping(isLoop);
        }
    }

    @Override
    public int getTotalLength() {
        return totlength;
    }

    private static class instanceHolder{
        private static final MusicPlayerImpl instance = new MusicPlayerImpl();
    }
    private MusicPlayerImpl(){
    }
    public static MusicPlayerImpl getInstance(){
        return instanceHolder.instance;
    }


}
