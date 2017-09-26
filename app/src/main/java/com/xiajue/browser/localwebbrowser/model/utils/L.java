package com.xiajue.browser.localwebbrowser.model.utils;

import android.util.Log;
  
/** 
 * Log 统一管理
 *  
 *  
 *  
 */  
public class L  
{  
  
    private L()  
    {  
        /* cannot be instantiated */  
        throw new UnsupportedOperationException("cannot be instantiated");  
    }  
  
    public static boolean isDebug = true;// 是否显示log、默认为true
    private static final String TAG = "xiaJue";

    public static void i(String msg)
    {  
        if (isDebug)  
            Log.i(TAG, msg);  
    }  
  
    public static void d(String msg)  
    {  
        if (isDebug)  
            Log.d(TAG, msg);  
    }  
  
    public static void e(String msg)  
    {  
        if (isDebug)  
            Log.e(TAG, msg);  
    }  
  
    public static void v(String msg)  
    {  
        if (isDebug)  
            Log.v(TAG, msg);  
    }  
  

    public static void i(String tag, String msg)  
    {  
        if (isDebug)  
            Log.i(tag, msg);  
    }  
  
    public static void d(String tag, String msg)  
    {  
        if (isDebug)  
            Log.i(tag, msg);  
    }  
  
    public static void e(String tag, String msg)  
    {  
        if (isDebug)  
            Log.i(tag, msg);  
    }  
  
    public static void v(String tag, String msg)  
    {  
        if (isDebug)  
            Log.i(tag, msg);  
    }  
}  