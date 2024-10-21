package cc.lx.utils;

/**
 * Cocos2dx 代理者
 */
public class Cocos2dxUtils {

    private static JsExecutor jsExecutor;

    public interface JsExecutor {
        void execute(String js);
    }

    public static void setJsExecutor(JsExecutor executor) {
        jsExecutor = executor;
    }

    public static void execute(String js) {
        if (jsExecutor != null) {
            jsExecutor.execute(js);
        }
    }
}

