package com.example.wangfujia.testandroid;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wangfujia.testandroid.service.MyService;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private TextView mTextMessage2;
    private TextView startServiceTv;
    private TextView bindServiceTv;
    final String TAG = "MainActivity";

    boolean isStart = false;

    private View mainContentView;
    private RecyclerView recyclerView;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mainContentView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mainContentView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"---onCreate---");
        setContentView(R.layout.activity_main);

        mainContentView = findViewById(R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.myRecyclerView);
        mTextMessage = (TextView) findViewById(R.id.message);
        mTextMessage2 = (TextView) findViewById(R.id.message2);
        startServiceTv = (TextView) findViewById(R.id.message3);
        bindServiceTv = (TextView) findViewById(R.id.message4);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        Menu menu = navigation.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setIcon(menuItem.isChecked()?R.mipmap.ic_check_true:R.drawable.ic_menu_home_normal);

//        mTextMessage.setClickable(true);
//        mTextMessage2.setClickable(true);
        mTextMessage.setOnClickListener(this);
        mTextMessage2.setOnClickListener(this);
        startServiceTv.setOnClickListener(this);
        bindServiceTv.setOnClickListener(this);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"---onRestart---");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"---onStart---");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG,"---onRestoreInstanceState---");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"---onResume---");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"---onPause---");
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d(TAG,"---onSaveInstanceState---");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"---onStop---");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"---onDestroy---");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message:
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(intent);
                break;
            case R.id.message2:
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setTitle("测试");
                dialog.show();
                break;
            case R.id.message3:
                startOrStopService();
                break;
            case R.id.message4:
                Intent intentService2 = new Intent(this,MyService.class);
                bindService(intentService2, serviceConnection, Context.BIND_AUTO_CREATE);
                break;
        }
    }

    private void threadStartService(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                startOrStopService();
            }
        }).start();



    }

    private void threadStartService2(){
        HandlerThread handlerThread = new HandlerThread("HandlerThread["+this.getClass().getName()+"]");
        handlerThread.start();
        Handler serviceHandler = new Handler(handlerThread.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.d(TAG, "serviceHandler--handleMessage---");
                startOrStopService();
            }
        };

        Message msg = serviceHandler.obtainMessage();

        serviceHandler.sendMessage(msg);
    }

    private void startOrStopService(){
        Intent intentService = new Intent(MainActivity.this, MyService.class);
        if (isStart){
            stopService(intentService);
            isStart = false;
        }else {
            startService(intentService);
            isStart = true;
        }
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected--name=" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected--name=" + name);
        }
    };


    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        Log.d(TAG, "isServiceWork--serviceName("+serviceName+")---isWork="+isWork);
        return isWork;
    }
}
