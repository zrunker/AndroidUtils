package cc.banzhi.android.androidutilslib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;

import cc.banzhi.android.androidutilslib.file.FileUtil;

/**
 * WebView生成图片工具类
 *
 * @author 邹峰立
 */
public class WebViewToPictureUtil {

    /**
     * 生成图片
     */
    private File generateFile(@NonNull Context context, @NonNull WebView webView) {
        File file = null;
        ToastUtil.shortToast(context, "图片生成中...");
        Bitmap bitmap = getWebViewBitmap(webView);
        if (bitmap != null) {
            try {
                String filePath = FileUtil.getSdPath(context) + File.separator + "androidutilslib" + File.separator + "shares" + File.separator;
                String fileName = System.currentTimeMillis() + ".jpg";
                File dir = new File(filePath);
                boolean bool = dir.exists();
                if (!bool) {
                    FileUtil.createSDDirs(filePath);
                }

                file = new File(filePath, fileName);
                FileOutputStream fOut = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
                fOut.flush();
                fOut.close();
            } catch (Exception var11) {
                var11.printStackTrace();
            } finally {
                bitmap.recycle();
                System.gc();
            }
        } else {
            ToastUtil.shortToast(context, "生成图片失败！");
        }

        return file;
    }

    /**
     * 获取整个WebView截图
     */
    private Bitmap getWebViewBitmap(@NonNull WebView webView) {
        Picture picture = webView.capturePicture();
        Bitmap bitmap = null;
        if (picture != null && picture.getWidth() > 0 && picture.getHeight() > 0) {
            bitmap = Bitmap.createBitmap(picture.getWidth(), picture.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            picture.draw(canvas);
        }
        return bitmap;
    }

}
