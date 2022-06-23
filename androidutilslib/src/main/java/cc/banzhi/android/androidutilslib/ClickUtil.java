package cc.banzhi.android.androidutilslib;

/**
 * 点击事件工具类
 *
 * @author zoufengli01
 */
public class ClickUtil {
    private static long lastClickTime;

    // 判断是否为快速点击事件
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500)
            return true;
        lastClickTime = time;
        return false;
    }
}