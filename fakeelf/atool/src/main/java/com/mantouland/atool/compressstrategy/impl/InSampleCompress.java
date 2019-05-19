package com.mantouland.atool.compressstrategy.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mantouland.atool.compressstrategy.CompressStrategy;
import com.mantouland.atool.compressstrategy.option.CompressOption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by asparaw on 2019/4/9.
 */
public class InSampleCompress implements CompressStrategy {


    private static class instanceHolder{
        private static final InSampleCompress instance = new InSampleCompress();
    }
    private InSampleCompress(){

    }
    public static InSampleCompress getInstance(){
        return instanceHolder.instance;
    }


    private static final int NON_OPTION = -1;
    private static final int ORIGINAL = 1;
    /***
     * option needs following parameters
     * maxsize means pic occupied bytes
     * @param bitmap
     * @param option
     * @return
     */
    @Override
    public Bitmap compress(Bitmap bitmap, CompressOption option) {
        int sampleSize=getSize(bitmap,option);
        if (sampleSize!=ORIGINAL){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            ByteArrayInputStream isBm = new ByteArrayInputStream(out.toByteArray());
            BitmapFactory.Options bfo = new BitmapFactory.Options();
            bfo.inJustDecodeBounds = true;
            bfo.inSampleSize = sampleSize;
            bitmap = BitmapFactory.decodeStream(isBm, null, bfo);
            try {
                out.close();
                isBm.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /***
     * get sample size
     * @return
     */
    public int getSize(Bitmap bitmap,CompressOption option){
        if (option.getMaxSize()!=NON_OPTION){
            int dataSize = bitmap.getByteCount() * option.getHeight();
            int maxSize = option.getMaxSize();
            int sampleSize= dataSize/maxSize;
            int resultSize=1;
            while (resultSize >sampleSize){
                resultSize*=2;
            }
            return resultSize;
        }else {
            return ORIGINAL;
        }
    }
}
