package cc.lx.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.IBinder;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;

import cc.lx.service.ScreenRecordService;

/**
 * 系统录屏
 */
public class SysRecorderWrapper {

    private final static int REQUEST_CODE = 101;

    //录屏工具
    private MediaProjectionManager mediaProjectionManager;
    //录屏服务
    private ScreenRecordService screenRecordService;

    private static SysRecorderWrapper instance;
    //单例模式
    private SysRecorderWrapper() {}

    public static SysRecorderWrapper getInstance() {
        if (instance == null) {
            instance = new SysRecorderWrapper();
        }
        return instance;
    }

    //连接服务成功与否，具体连接过程
    //调用连接接口，实现连接，回调连接结果
    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            //服务连接成功，需要通过Binder获取服务，达到Activity和Service通信的目的
            //获取Binder
            ScreenRecordService.ScreenRecordBinder binder = (ScreenRecordService.ScreenRecordBinder) iBinder;
            //通过Binder获取Service
            screenRecordService = binder.getScreenRecordService();
            //获取到服务，初始化录屏管理者
            mediaProjectionManager = (MediaProjectionManager) Utils.getApp().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            //通过管理者，创建录屏请求，通过Intent
            Intent captureIntent = mediaProjectionManager.createScreenCaptureIntent();
            //将请求码作为标识一起发送，调用该接口，需有返回方法
            ActivityUtils.getTopActivity().startActivityForResult(captureIntent, REQUEST_CODE);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //连接失败
            ToastUtils.showShort("Record service disconnected, please try again!");
        }
    };

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //首先判断请求码是否一致，结果是否ok
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //录屏请求成功，使用工具MediaProjection录屏
            //从发送获得的数据和结果中获取该工具
            MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
            //将该工具给Service，并一起传过去需要录制的屏幕范围的参数
            if (screenRecordService != null) {
                screenRecordService.setMediaProjection(mediaProjection);
                //获取录屏屏幕范围参数
                DisplayMetrics metrics = new DisplayMetrics();
                ActivityUtils.getTopActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                screenRecordService.setConfig(metrics.widthPixels, metrics.heightPixels, metrics.densityDpi);
                screenRecordService.startRecord();
            }
        }
    }

    //连接服务
    private void connectService() {
        Context context = ActivityUtils.getTopActivity();
        //通过intent为中介绑定Service，会自动create
        Intent intent = new Intent(context, ScreenRecordService.class);
        //绑定过程连接，选择绑定模式
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void startRecord() {
        //参数传过去以后，如果在录制，提示
        if (screenRecordService != null && screenRecordService.isRunning()) {
            ToastUtils.showShort("Recording, don't repeat!");
        } else if (screenRecordService != null && !screenRecordService.isRunning()) {
            //没有录制，就开始录制，弹出提示，返回主界面开始录制
            screenRecordService.startRecord();
        } else if (screenRecordService == null) {
            connectService();
        }
    }

    public void stopRecord() {
        if(screenRecordService == null)  {
            ToastUtils.showShort("Record is not started!");
            return;
        }
        if (!screenRecordService.isRunning()) {
            //没有在录屏，无法停止，弹出提示
            ToastUtils.showShort("Not recording, please start first!");
        } else {
            //停止录屏
            screenRecordService.stopRecord();
        }
    }

    public void onStop() {
        if (screenRecordService != null) {
            screenRecordService.stopRecord();
        }
    }

    public void onDestroy() {
        if (screenRecordService != null) {
            ActivityUtils.getTopActivity().unbindService(serviceConnection);
        }
    }
}
