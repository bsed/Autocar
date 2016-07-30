package com.huanyun.autocar.constant;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2016/3/14.
 */
public class CommonSettingProvider {

    private static final Object mLock_Main_Setting = new Object();
    private static final String MAIN_SETTING = "main_setting";

    public static void setUserId(Context context, String userid) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("user_id", userid);
            editor.commit();
        }
    }

    public static String getUserId(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("user_id", "");
        }
    }

    public static void setUserName(Context context, String username) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("user_name", username);
            editor.commit();
        }
    }

    public static String getUserName(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("user_name", "");
        }
    }

    public static void setNickName(Context context, String nickname) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("nickname", nickname);
            editor.commit();
        }
    }

    public static String getNickName(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("nickname", "");
        }
    }

    public static void setPwd(Context context, String pwd) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("pwd", pwd);
            editor.commit();
        }
    }

    public static String getPwd(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("pwd", "");
        }
    }

    public static void setLongitude(Context context, String longitude) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("longitude", longitude);
            editor.commit();
        }
    }

    public static String getLongitude(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("longitude", "");
        }
    }

    public static void setLatitude(Context context, String latitude) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            SharedPreferences.Editor editor = appLockListSettings.edit();
            editor.putString("latitude", latitude);
            editor.commit();
        }
    }

    public static String getLatitude(Context context) {
        synchronized (mLock_Main_Setting) {
            SharedPreferences appLockListSettings = context.getSharedPreferences(MAIN_SETTING, 0);
            return appLockListSettings.getString("latitude", "");
        }
    }

}
