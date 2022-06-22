package cc.banzhi.android.androidutilslib;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Url处理类
 *
 * @author 邹峰立
 */
public class UrlUtil {

    /**
     * 获取Document
     *
     * @param url 链接
     */
    private static Document getDocument(@NonNull String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * 通过Url获取title信息，只能运行在子线程中
     *
     * @param url 待处理的title信息
     */
    public static String getUrlTitle(String url) {
        String title = null;
        if (!TextUtils.isEmpty(url)) {
            try {
                Document doc = getDocument(url);
                if (doc != null) {
                    title = doc.title();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return title;
    }

    /**
     * 获取网页中meta标签的description值
     *
     * @param url 链接
     */
    public static String getUrlDescription(String url) {
        if (!TextUtils.isEmpty(url)) {
            try {
                Document doc = getDocument(url);
                if (doc != null) {
                    Elements attr = doc.select("meta[name=description]");
                    if (attr != null) {
                        Element element = attr.get(0);
                        return element.attr("content");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取页面中所有img标签
     *
     * @param url 链接
     */
    public static ArrayList<String> getUrlImgPaths(String url) {
        ArrayList<String> imgPaths = new ArrayList<>();
        if (!TextUtils.isEmpty(url)) {
            Document doc = getDocument(url);
            Elements attr = doc.getElementsByTag("img");
            for (Element element : attr)
                imgPaths.add(element.attr("src"));
        }
        return imgPaths;
    }
}
