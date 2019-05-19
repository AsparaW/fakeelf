package com.mantouland.fakeelf.component.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mantouland.atool.HTTPHelper;
import com.mantouland.atool.callback.impl.PicCallBack;
import com.mantouland.fakeelf.R;
import com.mantouland.fakeelf.component.service.MusicService;
import com.mantouland.fakeelf.controller.MusicController;

public class Detail extends AppCompatActivity implements View.OnClickListener {

    private MusicService.MyBinder musicBinder;
    private ServiceConnection connection;

    private int id=0;
    private Bitmap picBitmap;
    private TextView author;


    private int nowTime;
    private int totTime;
    private ImageView detBack;
    private TextView mname;
    private boolean isThumb=false;
    private boolean isLike =false;
    private boolean isPlayin=false;
    private ImageView lastMusic;
    private ImageView nextMusic;
    private ImageView download;
    private ImageView thumbup;
    private ImageView star;
    private ImageView comment;
    private ImageView playPause;
    private SeekBar seekBar;
    private TextView pro1,pro2;

    private ImageView bigPic;
    private boolean prolock=false;
    private int time;

    private Handler handler;
    private static final String TAG= "TEST DETAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        bindView();
        handler=new Handler();
        Intent musicIntent =new Intent(this,MusicService.class);
        Log.d(TAG, "onCreate: ");
        connection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicBinder= (MusicService.MyBinder) service;
                MusicController.getInstance().setBinder(musicBinder);
                handler.postDelayed(overChecker,1000);
                //refreshUI();
                Log.d(TAG, "onServiceConnected: "+musicBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(musicIntent,connection,BIND_AUTO_CREATE);
        isPlayin=MusicController.getInstance().isPlaying();
        Log.d(TAG, "onCreate: "+isPlayin);
        Log.d(TAG, "onCreate: "+ MusicController.getInstance().checkProgress());
        if (isPlayin){
            isPlayin=true;

            playPause.setImageResource(R.mipmap.ic_play_running);
        }else {
            //pause
            isPlayin=false;
            playPause.setImageResource(R.mipmap.ic_play_pause);

        }

        pro2.setText(millis2Str(MusicController.getInstance().getTotLength()));
        prolock=false;
        handler.post(UiRunnable);
        handler.post(seekChecker);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.i_back:
                MusicController.getInstance().stop();
                MusicController.getInstance().playLast();
                pro2.setText(millis2Str(MusicController.getInstance().getTotLength()));
                handler.post(UiRunnable);
                break;
            case R.id.i_forward:
                MusicController.getInstance().stop();
                MusicController.getInstance().playNext();
                pro2.setText(millis2Str(MusicController.getInstance().getTotLength()));
                handler.post(UiRunnable);
                break;
            case R.id.i_play:
                if (isPlayin){
                    //pause
                    isPlayin=false;
                    prolock=true;
                    playPause.setImageResource(R.mipmap.ic_play_pause);
                }else {
                    isPlayin=true;
                    prolock=false;
                    playPause.setImageResource(R.mipmap.ic_play_running);
                }
                MusicController.getInstance().pause();
                break;
            case R.id.i_coment:
                //
                break;
            case R.id.i_thumb:
                if (isThumb){
                    isThumb=false;
                    thumbup.setImageResource(R.mipmap.ic_like_off);
                }else {
                    isThumb=true;
                    thumbup.setImageResource(R.mipmap.ic_like_on);
                }
                break;
            case R.id.i_down:
                    //
                break;
            case R.id.i_star:
                if (isLike){
                    isLike=false;
                    star.setImageResource(R.mipmap.ic_star_off);
                }else {
                    isLike=true;
                    star.setImageResource(R.mipmap.ic_star_on);
                }
                break;
            case R.id.det_back:
                Intent intent=new Intent(this,MainActivity.class);
                v.getContext().startActivity(intent);
                break;

        }
    }
    void bindView(){
        author=findViewById(R.id.det_author);
        lastMusic=findViewById(R.id.i_back);
        nextMusic=findViewById(R.id.i_forward);
        download = findViewById(R.id.i_down);
        thumbup = findViewById(R.id.i_thumb);
        star= findViewById(R.id.i_star);
        comment=findViewById(R.id.i_coment);
        playPause=findViewById(R.id.i_play);
        bigPic=findViewById(R.id.bigpic);
        seekBar=findViewById(R.id.seekBar);
        pro1=findViewById(R.id.pro1);
        pro2=findViewById(R.id.pro2);
        detBack=findViewById(R.id.det_back);
        mname=findViewById(R.id.musicName);
        lastMusic.setOnClickListener(this);
        nextMusic.setOnClickListener(this);
        download.setOnClickListener(this);
        thumbup.setOnClickListener(this);
        star.setOnClickListener(this);
        comment.setOnClickListener(this);
        playPause.setOnClickListener(this);
        bigPic.setOnClickListener(this);
        seekBar.setOnClickListener(this);
        detBack.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                prolock=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prolock=false;
                Log.d(TAG, "onStopTrackingTouch: "+seekBar.getProgress());
                MusicController.getInstance().seek(seekBar.getProgress());
            }
        });
    }

    Runnable overChecker = new Runnable() {
        @Override
        public void run() {
            time=MusicController.getInstance().checkProgress();
            int tot=MusicController.getInstance().getTotLength();


            if (time==-1){
                handler.post(UiRunnable);
                handler.postDelayed(this,1000);
            }else {
                handler.post(seekChecker);
                handler.postDelayed(this,1000);
            }
        }
    };

    Runnable   UiRunnable =new  Runnable(){
        @Override
        public void run() {
            String musicStringName = MusicController.getInstance().getNowPlaying().getName();
            String authorName= MusicController.getInstance().getNowPlaying().getAr().get(0).getName();
            musicStringName=musicStringName.trim();
            authorName=authorName.trim();
            int tid=id+1;
            id=tid;
            HTTPHelper.getInstance().doPic(MusicController.getInstance().getNowPlaying().getAl().getPicUrl(), new PicCallBack() {
                @Override
                public void onSuccess(Bitmap bitmap) {
                    if (tid==id){
                        picBitmap=bitmap;
                        handler.post(picRun);
                    }
                }

                @Override
                public void onFail(Exception e) {

                }
            });
            author.setText(authorName);

            mname.setText(musicStringName);
        }
    };

    Runnable picRun = new Runnable() {
        @Override
        public void run() {
            bigPic.setImageBitmap(picBitmap);
        }
    };

    Runnable seekChecker = new Runnable() {
        @Override
        public void run() {
            time=MusicController.getInstance().checkProgress();
            int tot=MusicController.getInstance().getTotLength();
            int progress=time*1000/tot;
            if (!prolock){
                seekBar.setProgress(progress);
                pro1.setText(millis2Str(time));
            }
        }
    };

    String millis2Str(int millis){
        millis=millis/1000;
        int min=millis/60;
        int sec=millis%60;
        if (sec>=10)
            return min+":"+sec;
        else
            return min+":0"+sec;
    }

}
