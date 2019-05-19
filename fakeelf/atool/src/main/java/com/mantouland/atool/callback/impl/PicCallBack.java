package com.mantouland.atool.callback.impl;

import android.graphics.Bitmap;

import com.mantouland.atool.callback.UniCallBack;


/**
 * Created by asparaw on 2019/4/3.
 */
public abstract class PicCallBack extends UniCallBack {
    public abstract void onSuccess(Bitmap bitmap);
    public abstract void onFail(Exception e);
}
