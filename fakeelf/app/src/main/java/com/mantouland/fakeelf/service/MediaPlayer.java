package com.mantouland.fakeelf.service;

/**
 * Created by asparaw on 2019/4/15.
 */
public interface MediaPlayer {
    void play(String path);
    void pause();
    void stop();
    int getTime();
    void seek(int time);
    boolean isPlaying();
    void setLoop(boolean isLoop);
    int getTotalLength();
}
