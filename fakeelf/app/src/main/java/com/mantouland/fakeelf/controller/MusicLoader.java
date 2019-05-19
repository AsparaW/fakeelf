package com.mantouland.fakeelf.controller;

import android.util.Log;

import com.mantouland.atool.HTTPHelper;
import com.mantouland.atool.callback.impl.JsonCallBack;
import com.mantouland.fakeelf.bean.ListBean;
import com.mantouland.fakeelf.bean.ListDetailBean;
import com.mantouland.fakeelf.bean.LyricBean;

import java.util.HashMap;

/**
 * Created by asparaw on 2019/5/18.
 */
public class MusicLoader {
    //singleton
    private static class instanceHolder{
        private static final MusicLoader instance = new MusicLoader();
    }
    public static MusicLoader getInstance(){
        return instanceHolder.instance;
    }
    private MusicLoader(){
        //DO_NOTHING
    }


    private static final String TAG= "LOADER";
    private static final String MOOD_LIST ="http://elf.egos.hosigus.com/getSongListID.php";
    private static final String DETAIL="http://elf.egos.hosigus.com/music/playlist/detail/";
    private static final String LRC="http://elf.egos.hosigus.com/lyric";


    private ListBean listBean;
    private ListDetailBean listDetailBean;

    /**
     * following is
     * http_loader
     */
    public void loadMood(String mood, final JsonCallBack jsonCallBack){
        String path= MOOD_LIST;

        path=path+"?type="+mood;
        Log.d(TAG, "loadMood: "+path);
        HTTPHelper.getInstance().doJsonString(path, null, null, false, new JsonCallBack() {
            @Override
            public void onSuccess(Object jsonBean) {
               listBean=(ListBean) jsonBean;
               jsonCallBack.onSuccess(listBean);
                Log.d(TAG, "onSuccess: "+listBean.getStatus());
            }
        }, ListBean.class);
        Log.d(TAG, "loadMood: ");
    }

   public void loadDetail(long id,final JsonCallBack jsonCallBack){
        String path=DETAIL;
        HashMap<String,String> map=new HashMap();
        map.put("id",String.valueOf(id) );
       Log.d(TAG, "loadDetail: "+path);
        HTTPHelper.getInstance().doJsonString(path, map, null, false, new JsonCallBack() {
            @Override
            public void onSuccess(Object jsonBean) {
                listDetailBean=(ListDetailBean) jsonBean;
                jsonCallBack.onSuccess(listDetailBean);
            }
        }, ListDetailBean.class);
    }

   public LyricBean loadLyric(String id){
        return null;
    }


    public void setList(ListBean listBean){
        this.listBean=listBean;
    }

    /**
     * following is
     * data automachine
     */
    private void toPlayList(){


    }
}
