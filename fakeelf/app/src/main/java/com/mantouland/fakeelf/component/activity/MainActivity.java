package com.mantouland.fakeelf.component.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mantouland.atool.callback.impl.JsonCallBack;
import com.mantouland.fakeelf.R;
import com.mantouland.fakeelf.bean.ListBean;
import com.mantouland.fakeelf.bean.ListDetailBean;
import com.mantouland.fakeelf.component.service.MusicService;
import com.mantouland.fakeelf.constant.APPConstant;
import com.mantouland.fakeelf.controller.MusicController;
import com.mantouland.fakeelf.controller.MusicLoader;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    
    private static final String TAG = "testmain";
    /**
     *
     */
    private ImageButton musicDetail;
    private TextView musicName;
    private TextView author;

    private ImageView moodPic;
    private ImageView moo2;
    private ImageView moo3;
    private ImageView moo4;
    private ImageView mooFrame;
    private ConstraintLayout mainLayout;
    private MusicService.MyBinder musicBinder;
    private ServiceConnection connection;
    private int time;

    private Handler handler=null;

    private String nowMood="HAPPY";
    private String[] moodList={"HAPPY","UNHAPPY","CLAM","EXCITING"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        askPermission();
        MusicController.getInstance().stop();
        setContentView(R.layout.activity_main);
        bindViews();
        handler=new Handler();
        Intent musicIntent =new Intent(this,MusicService.class);
        Log.d(TAG, "onCreate: ");
        connection= new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicBinder= (MusicService.MyBinder) service;
                MusicController.getInstance().setBinder(musicBinder);
                handler.postDelayed(progressChecker,3000);
                //refreshUI();
                Log.d(TAG, "onServiceConnected: "+musicBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(musicIntent,connection,BIND_AUTO_CREATE);
        initMusicController();
    }

    void bindViews(){
        musicName=findViewById(R.id.t_musicname);
        author=findViewById(R.id.t_author);
        moodPic=findViewById(R.id.b_mood);
        moo2=findViewById(R.id.i_m1);
        moo3=findViewById(R.id.i_m2);
        moo4=findViewById(R.id.i_m3);
        mooFrame=findViewById(R.id.i_mood);
        moodPic.setOnClickListener(this);
        moo2.setOnClickListener(this);
        moo3.setOnClickListener(this);
        moo4.setOnClickListener(this);
        mainLayout=findViewById(R.id.mainl);
        mainLayout.setOnClickListener(this);
        moo2.setVisibility(View.INVISIBLE);
        moo3.setVisibility(View.INVISIBLE);
        moo4.setVisibility(View.INVISIBLE);
        musicDetail=findViewById(R.id.b_detail);
        musicDetail.setOnClickListener(this);
        mooFrame.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_mood:
                int cur=0;
                for (int i=0;i<moodList.length;i++){
                    if (nowMood.equals(moodList[i])){
                        cur=i;
                        break;
                    }
                }
                String temp=moodList[0];
                moodList[0]=moodList[cur];
                moodList[cur]=temp;
                //123 is remaining
                moo2.setImageResource(mood2Mip(moodList[1]));
                moo3.setImageResource(mood2Mip(moodList[2]));
                moo4.setImageResource(mood2Mip(moodList[3]));

                hideMood();
                break;
            case R.id.i_m2:
                if (moo3.getVisibility()==View.VISIBLE){
                    nowMood=moodList[2];
                    hideMood();
                    MusicController.getInstance().stop();
                    moodPic.setImageResource(mood2Mip(moodList[2]));
                    initMusicController();
                }
                break;
            case R.id.i_m3:
                if (moo4.getVisibility()==View.VISIBLE){
                    nowMood=moodList[3];
                    hideMood();
                    MusicController.getInstance().stop();
                    moodPic.setImageResource(mood2Mip(moodList[3]));
                    initMusicController();
                }
                break;
            case R.id.i_m1:
                if (moo2.getVisibility()==View.VISIBLE){
                    nowMood=moodList[1];
                    hideMood();
                    MusicController.getInstance().stop();
                    moodPic.setImageResource(mood2Mip(moodList[1]));
                    initMusicController();
                }
                break;
            case R.id.b_detail:
                Log.d(TAG, "onClick: "+"gotodetail");
                Bundle bundle = new Bundle();
                Intent intent=new Intent(v.getContext(),Detail.class);
                intent.putExtras(bundle);
                v.getContext().startActivity(intent);
                break;
            case R.id.mainl:
                Log.d(TAG, "onClick: "+"DEFAULTCLi");
                mooFrame.setVisibility(View.INVISIBLE);
                moo2.setVisibility(View.INVISIBLE);
                moo3.setVisibility(View.INVISIBLE);
                moo4.setVisibility(View.INVISIBLE);
            break;

        }
    }
    void hideMood(){
        if (mooFrame.getVisibility()==View.VISIBLE){
            mooFrame.setVisibility(View.INVISIBLE);
            moo2.setVisibility(View.INVISIBLE);
            moo3.setVisibility(View.INVISIBLE);
            moo4.setVisibility(View.INVISIBLE);
        }else {
            moodPic.setVisibility(View.INVISIBLE);
            mooFrame.setVisibility(View.VISIBLE);
            moodPic.setVisibility(View.VISIBLE);
            moo2.setVisibility(View.VISIBLE);
            moo3.setVisibility(View.VISIBLE);
            moo4.setVisibility(View.VISIBLE);
        }

    }
    int mood2Mip(String string){
        switch (string) {
            case "HAPPY":
                return R.mipmap.ic_mood_happy;
            case "UNHAPPY":
                return R.mipmap.ic_mood_unhappy;
            case "CLAM":
                return R.mipmap.ic_mood_clam;
            case "EXCITING":
                return R.mipmap.ic_mood_exciting;
        }
        return 0;
    }
    private boolean checkPermission() {
        boolean hasPermission = true;
            for (String permission:APPConstant.APP_PERMISSION){
                Log.d(TAG, "checkPermission: "+permission);
                hasPermission=ContextCompat.checkSelfPermission(this,permission)==PackageManager.PERMISSION_GRANTED;
                if (!hasPermission)
                    break;
            }
        return hasPermission;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions
                (this, APPConstant.APP_PERMISSION, 1);
    }

    private void askPermission(){
        if (!checkPermission())
            requestPermission();
    }

    private void initMusicController(){
        //set first data

        updateMood(nowMood);
    }
    private void updateMood(String mood){

        //
        MusicLoader.getInstance().loadMood(mood, new JsonCallBack() {
            @Override
            public void onSuccess(Object jsonBean) {
                ListBean listBean=(ListBean)jsonBean;
                Log.d(TAG, "onSuccess: "+listBean.getData().getId());
                MusicLoader.getInstance().loadDetail(listBean.getData().getId(), new JsonCallBack() {
                    @Override
                    public void onSuccess(Object jsonBean) {
                        ListDetailBean listDetailBean = (ListDetailBean) jsonBean;
                        MusicController.getInstance().setMusicList(listDetailBean);
                        MusicController.getInstance().playThis();
                        Log.d(TAG, "onSuccess: ");
                        handler.post(UiRunnable);
                    }
                });
                //set over
            }
        });
    }
    private void refreshUI(){

    }

    Runnable   UiRunnable =new  Runnable(){
        @Override
        public void run() {
            String musicStringName = MusicController.getInstance().getNowPlaying().getName();
            String authorName= MusicController.getInstance().getNowPlaying().getAr().get(0).getName();
            musicStringName=musicStringName.trim();
            authorName=authorName.trim();
            musicName.setText(musicStringName);
            author.setText(authorName);
        }
    };
    Runnable progressChecker = new Runnable() {
        @Override
        public void run() {
            time=MusicController.getInstance().checkProgress();
            handler.post(UiRunnable);
                handler.postDelayed(this,300);
        }
    };


}
