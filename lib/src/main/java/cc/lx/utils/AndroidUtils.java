package cc.lx.utils;

import android.os.Handler;
import android.os.Looper;

public class AndroidUtils {

    private static Handler handler = null;

    public static void runOnUiThread(Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
        } else {
            if (handler == null) {
                handler = new Handler(Looper.getMainLooper());
            }
            handler.post(runnable);
        }
    }

    /**
     * 开始录制
     */
    public static void startRecord() {
        SysRecorderWrapper.getInstance().startRecord();
    }

    /**
     * 停止录制
     */
    public static void stopRecord() {
        SysRecorderWrapper.getInstance().stopRecord();
    }
}
