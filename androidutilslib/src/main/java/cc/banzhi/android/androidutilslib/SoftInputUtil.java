package cc.banzhi.android.androidutilslib;

import android.content.Context;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软盘工具类
 *
 * @author 邹峰立
 */
public class SoftInputUtil {
    private static SoftInputUtil softInputUtil;

    public static SoftInputUtil getSoftInputUtil() {
        if (softInputUtil == null)
            synchronized (SoftInputUtil.class) {
                softInputUtil = new SoftInputUtil();
            }
        return softInputUtil;
    }

    /**
     * 展示软盘
     *
     * @param context  上下文对象
     * @param editText 待获取焦点EditText
     */
    public void showSoftInput(Context context, EditText editText) {
        if (editText != null && context != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    editText.setFocusable(true);
                    editText.setFocusableInTouchMode(true);
                    editText.requestFocus();

                    InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (inputManager != null) {
                        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }, 100);
        }
    }

    /**
     * 关闭软键盘
     *
     * @param context  上下文对象
     * @param editText 待获取焦点EditText
     */
    public void closeSoftInput(Context context, EditText editText) {
        if (editText != null && context != null) {
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
            editText.clearFocus();

            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }
}
