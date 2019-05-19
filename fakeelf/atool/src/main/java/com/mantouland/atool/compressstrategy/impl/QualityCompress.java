package com.mantouland.atool.compressstrategy.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mantouland.atool.compressstrategy.CompressStrategy;
import com.mantouland.atool.compressstrategy.option.CompressOption;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by asparaw on 2019/4/12.
 */
public class QualityCompress implements CompressStrategy {
    @Override
    public Bitmap compress(Bitmap bitmap, CompressOption option) {
        int quality = 85;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,quality,byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream= new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        bitmap=BitmapFactory.decodeStream(byteArrayInputStream);
        while (byteArrayOutputStream.toByteArray().length > option.getMaxSize()&& quality>10 ) {
            quality -= 5;
            byteArrayOutputStream.reset();
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, byteArrayOutputStream);
        }
        try {
            byteArrayOutputStream.close();
            byteArrayInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
