package com.example.wangfujia.testandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.wangfujia.testandroid.service.MyService;

public class Main2Activity extends AppCompatActivity {

    private final String TAG = "Main2Activity";
    TextView textView;
    Button button1;
    MyService myService;
    boolean isBind = false;
    Messenger messenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        textView = (TextView) findViewById(R.id.textview);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messenger != null){
                    Message message = Message.obtain(null,MyService.DO_MESSENGER);
                    try {
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "--onStart--");
        Log.d(TAG,"---satrtservice---this.process="+ Process.myPid());
        Intent intentService2 = new Intent();
        intentService2.setAction("com.wangfj.testService");
        intentService2.setPackage("com.example.wangfujia.testandroid");
        bindService(intentService2, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "--onStop--");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "--onDestroy--");
        if (isBind){
            unbindService(serviceConnection);
            isBind = false;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected--name=" + name);
            isBind = true;
            /**
             * 通过binder方式通信
             */
//            MyService.MyBinder myBinder = (MyService.MyBinder) service;
//            myService = myBinder.getService();
//            int num = myService.getRandomNumber();
//            textView.setText(String.valueOf(num));
            /**
             * 通过Messenger方式通信
             * 特点：任务放入队列，不用担心多线程问题，但是只能处理串行问题，不能处理大量并行请求
             */
           messenger = new Messenger(service);
            /**
             * 使用AIDL方式通信
             *
             */
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected--name=" + name);
            isBind = false;
        }
    };
}
