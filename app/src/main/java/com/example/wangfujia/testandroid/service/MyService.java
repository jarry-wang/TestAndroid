package com.example.wangfujia.testandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {
    private final String TAG = "MyService";
    private final MyBinder myBinder = new MyBinder();
    public static final int DO_MESSENGER = 0X01;

    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"---onCreate---");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG,"---onBind---");
//        return myBinder;
        Messenger messenger = new Messenger(MyHandler);
        return messenger.getBinder();
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG,"---onStart---");
//        while (true){
//            Log.d(TAG,"---isRunning---");
//        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"---onStartCommand---this.process="+ Process.myPid());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(TAG,"---onRebind---");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"---onUnbind---");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"---onDestroy---");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.d(TAG,"---onLowMemory---");
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.d(TAG,"---onTrimMemory---");
    }

    private Handler MyHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case DO_MESSENGER:
                    Log.d(TAG,"activity--user messenger--to--control service--");
                    break;
            }
        }
    };

    public  class MyBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }

    public int getRandomNumber (){
        return new Random().nextInt();
    }
}
