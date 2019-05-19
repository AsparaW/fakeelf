package com.mantouland.fakeelf.controller;

import android.util.Log;

import com.mantouland.fakeelf.bean.ListDetailBean;
import com.mantouland.fakeelf.component.service.MusicService;
import com.mantouland.fakeelf.constant.APPConstant;

import java.util.List;
import java.util.Random;


/**
 * Created by asparaw on 2019/4/16.
 */
public class MusicController {
    private static final String TAG = MusicController.class.toString();
    private MusicService.MyBinder binder;
    private List<ListDetailBean.PlaylistBean.TracksBean> musicList;
    private int nowPlayingIndex = 0;
    private boolean isLoop;
    private boolean isPlaying = false;
    public boolean nonempty = false;

    public boolean isPlaying() {
        return isPlaying;
    }

    private static class instanceHolder {
        private static final MusicController instance = new MusicController();
    }

    public static MusicController getInstance() {
        return instanceHolder.instance;
    }

    private MusicController() {
        //DO_NOTHING
    }

    public void setMusicList(List<ListDetailBean.PlaylistBean.TracksBean> musicList) {
        this.musicList = musicList;
    }

    public void setMusicList(ListDetailBean listDetailBean){
        this.musicList=data2list(listDetailBean);
        this.nowPlayingIndex=new Random().nextInt(musicList.size());
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
        binder.setLoop(isLoop);
    }

    public boolean isLoop() {
        return isLoop;
    }

    public List getMusicList() {
        return musicList;
    }

    public void setBinder(MusicService.MyBinder binder) {
        Log.d(TAG, this.binder + "setBinder: " + binder);
        this.binder = binder;
    }

    private void play(String path) {
        isPlaying = true;
        nonempty = true;
        if (binder != null) {
            Log.d(TAG, "ready to play: " + path + " at:" + nowPlayingIndex);
            path = path.trim();
            binder.play(path);
            binder.setLoop(isLoop);
        }
    }

    /**
     * 假如没有指定上下曲，那么就随机播放
     * @return
     */
    public int checkProgress(){
        if (!binder.isPlaying()&&isPlaying){
            return -1;
        }
        Log.d(TAG, "checkProgress: "+binder.getTime());
        return binder.getTime();
    }

    public void playRandom(){
        int temp = new Random().nextInt(musicList.size());
        while (temp==nowPlayingIndex){
            temp = new Random().nextInt(musicList.size());
        }
        nowPlayingIndex=temp;
        playThis();
    }

    public int getTotLength(){
        return binder.getTotal();
    }
    /**
     * pause and continue
     */
    public void pause() {
        binder.pause();
        isPlaying = binder.isPlaying();
    }

    public void playNext() {
        play(id2url(musicList.get(musicLooper(true)).getId()));
    }

    public void playThis() {
        play(id2url(musicList.get(nowPlayingIndex).getId()));
    }

    public void playLast() {
        play(id2url(musicList.get(musicLooper(false)).getId()));
    }

    public void stop() {
        isPlaying = false;
        nonempty = false;
        if (binder != null) {
            Log.d(TAG, "stop: ");
            binder.stop();
        }
    }


    private int musicLooper(boolean asc) {
        if (asc) {
            nowPlayingIndex++;
            if (nowPlayingIndex == musicList.size()) {
                nowPlayingIndex = 0;
            }
        } else {
            nowPlayingIndex--;
            if (nowPlayingIndex < 0) {
                nowPlayingIndex = musicList.size() - 1;
            }
        }
        return nowPlayingIndex;
    }

    public ListDetailBean.PlaylistBean.TracksBean getNowPlaying() {
        //Log.d(TAG, "getNowPlaying: " + musicList.get(nowPlayingIndex).getName() + " at" + nowPlayingIndex);
        return musicList.get(nowPlayingIndex);
    }

    public void seek(int dur){
        //1000=tot
        int tot=binder.getTotal();
        int time=tot/1000*dur;
        Log.d(TAG, "seek: "+time);
        binder.seek(time);
    }

    /**
     * data parser
     * <p>
     * make id 2 url
     */

    String id2url(long id) {
        String url;
        url = APPConstant.NETEASE + id + ".mp3";
        return url;
    }

    List<ListDetailBean.PlaylistBean.TracksBean> data2list(ListDetailBean listDetailBean){
        return listDetailBean.getPlaylist().getTracks();
    }
}
