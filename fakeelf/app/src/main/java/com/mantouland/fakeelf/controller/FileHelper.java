package com.mantouland.fakeelf.controller;

/**
 * Created by asparaw on 2019/4/18.
 * abandoned
 *
 */

public class FileHelper {


/*    private String SDCardPath = APPConstant.sdcardPath;
    private String appMusicPath =APPConstant.APP_MUSIC_PATH;
    private String[] supportedFormat = APPConstant.supportedFormat;
    private FileHelper(){
        //makedir
        File path = new File(appMusicPath);
        if (!path.isDirectory()){
            path.mkdir();
        }
    }

    private static class InstanceHolder{
        private static final FileHelper instance = new FileHelper();
    }
    public static FileHelper getInstance(){
        return InstanceHolder.instance;
    }


    public List<MusicBean> searchSDCard(){
        List<MusicBean> musicBeanList = new ArrayList<>();
        File dirPath=new File(appMusicPath);
        File files[]=dirPath.listFiles();
        if (files!=null){
            for (File file : files) {
                String name = file.getName();
                String suffix = name.substring(name.lastIndexOf('.'));
                for (String s : supportedFormat) {
                    if (suffix.equals(s)){
                        //DO
                        MusicBean temp=new MusicBean();
                        temp.setName(name);
                        temp.setPath(file.getAbsolutePath());
                        musicBeanList.add(temp);
                    }
                }
            }
        }
        return musicBeanList;
    }*/
}
