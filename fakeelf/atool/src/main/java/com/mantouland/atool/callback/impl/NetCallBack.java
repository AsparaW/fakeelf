package com.mantouland.atool.callback.impl;


import com.mantouland.atool.HTTPHelper;
import com.mantouland.atool.callback.UniCallBack;

/**
 * Created by asparaw on 2019/3/29.
 */
public abstract class NetCallBack extends UniCallBack {

    public abstract void onSuccess(HTTPHelper.HTTPContent httpContent);
}
