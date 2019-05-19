package com.mantouland.atool.compressstrategy;

import android.graphics.Bitmap;

import com.mantouland.atool.compressstrategy.option.CompressOption;


/**
 * Created by asparaw on 2019/4/9.
 */
public interface CompressStrategy {
    Bitmap compress(Bitmap bitmap, CompressOption option);
}
