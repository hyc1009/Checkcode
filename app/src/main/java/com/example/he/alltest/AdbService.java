package com.example.he.alltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ================================================
 * 作    者：何云超
 * 版    本：
 * 创建日期：2018/1/3
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class AdbService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Process process =  Runtime.getRuntime().exec("adb shell input tap 362 353");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
