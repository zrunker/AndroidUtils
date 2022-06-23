package cc.banzhi.android.androidutilslib;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

/**
 * 剪切板工具类
 *
 * @author 邹峰立
 */
public class ClipboardUtil {

    /**
     * 复制到剪切板
     *
     * @param context  上下文对象
     * @param keywords 复制关键词
     */
    public static void copyToClipboard(Context context, String keywords) {
        // 获取剪贴板管理器
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("simpleText", keywords);
        // 将ClipData内容放到系统剪贴板里
        if (cm != null)
            cm.setPrimaryClip(mClipData);
    }

    /**
     * 获取剪切板内容
     */
    public static String getClipboardText(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && manager.hasPrimaryClip()) {
            ClipData clipData = manager.getPrimaryClip();
            if (clipData != null && manager.getPrimaryClip().getItemCount() > 0) {
                CharSequence text = manager.getPrimaryClip().getItemAt(0).getText();
                String textStr = String.valueOf(text);
                if (!TextUtils.isEmpty(textStr)) {
                    return textStr.trim();
                }
            }
        }
        return null;
    }

    /**
     * 清空剪切板
     */
    public static void clearClipboard(Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (manager != null && manager.hasPrimaryClip()) {
            ClipData clipData = manager.getPrimaryClip();
            if (clipData != null && manager.getPrimaryClip().getItemCount() > 0) {
                manager.setPrimaryClip(manager.getPrimaryClip());
                manager.setPrimaryClip(ClipData.newPlainText("", ""));
            }
        }
    }

}