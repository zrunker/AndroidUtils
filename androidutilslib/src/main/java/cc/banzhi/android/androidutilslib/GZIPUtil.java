package cc.banzhi.android.androidutilslib;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * GZIP压缩封装工具类
 *
 * @author 邹峰立
 */
public class GZIPUtil {

    /**
     * 字符串的压缩
     *
     * @param str 待压缩的字符串
     * @return 返回压缩后的字符串
     */
    public static String compress(String str) {
        try {
            if (null == str || str.length() <= 0) {
                return str;
            }
            // 创建一个新的输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 使用默认缓冲区大小创建新的输出流
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            // 将字节写入此输出流
            gzip.write(str.getBytes(Charset.forName("UTF-8"))); // 因为后台默认字符集有可能是GBK字符集，所以此处需指定一个字符集
            gzip.close();
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString("ISO-8859-1");
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 字符串的解压
     *
     * @param str 对字符串解压
     * @return 返回解压缩后的字符串
     */
    public static String unCompress(String str) {
        try {
            if (null == str || str.length() <= 0) {
                return str;
            }
            // 创建一个新的输出流
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            // 创建一个 ByteArrayInputStream，使用 buf 作为其缓冲 区数组
            ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes(Charset.forName("ISO-8859-1")));
            // 使用默认缓冲区大小创建新的输入流
            GZIPInputStream gzip = new GZIPInputStream(in);
            byte[] buffer = new byte[256];
            int n;

            // 将未压缩数据读入字节数组
            while ((n = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
            // 使用指定的 charsetName，通过解码字节将缓冲区内容转换为字符串
            return out.toString("utf-8");
        } catch (Exception e) {
            return "";
        }
    }

}