package com.mantouland.fakeelf.constant;

import android.Manifest;
import android.os.Environment;

/**
 * Created by asparaw on 2019/4/15.
 */
public class APPConstant {
    /***
     * string test data
     */

    public static final int OK =1;

    public static final int MOOD_HAPPY =1;
    public static final int MOOD_UNHAPPY =2;
    public final int MOOD_CALM =3;
    public static final int MOOD_EXC =4;

    public final static String NETEASE ="http://music.163.com/song/media/outer/url?id=";


    public static final String sdcardPath=Environment.getExternalStorageDirectory().getPath();

    public static final String[] supportedFormat={".mp3",".flac"};

    public static final String APP_MUSIC_PATH=sdcardPath+"/MushroomMusic";
    public static final String[] APP_PERMISSION={Manifest.permission.INTERNET,Manifest.permission.
            READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE};


    //singleton
    private static final APPConstant  instance= new APPConstant();
    private APPConstant(){
        //DO_NOTHING
    }
    public static APPConstant getInstance (){
        return instance;
    }
}
