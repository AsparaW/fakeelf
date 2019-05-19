package com.mantouland.fakeelf.component.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.mantouland.fakeelf.service.impl.MusicPlayerImpl;


public class MusicService extends Service {
    private static final String TAG = "MusicService";
    private MyBinder myBinder = new MyBinder();
    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        MusicPlayerImpl.getInstance().stop();
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return myBinder;
    }
    public class MyBinder extends Binder {

        public void play(String path){
            MusicPlayerImpl.getInstance().play(path);
        }
        public int getTime(){
            return MusicPlayerImpl.getInstance().getTime();
        }
        public void stop(){
            MusicPlayerImpl.getInstance().stop();
        }
        public void pause(){
            MusicPlayerImpl.getInstance().pause();
        }
        public boolean isPlaying(){
            return MusicPlayerImpl.getInstance().isPlaying();
        }
        public void setLoop(boolean isLoop){
            MusicPlayerImpl.getInstance().setLoop(isLoop);
        }

        public int getTotal(){
            return MusicPlayerImpl.getInstance().getTotalLength();
        }
        public void seek(int time){
             MusicPlayerImpl.getInstance().seek(time);
        }
    }


}
