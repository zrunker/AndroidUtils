package cc.banzhi.android.androidutilslib;

import android.annotation.SuppressLint;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 数字工具类
 *
 * @author 邹峰立
 */
public class NumUtil {

    /**
     * 格式化数字，以w/k为单位
     *
     * @param num 待格式化数据
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatNum(int num) {
        String formatNum = num + "";
        if (num >= 10000) {
            float newNum = num / (float) 10000;
            formatNum = String.format("%.2f", newNum) + "w";
        } else if (num >= 1000) {
            float newNum = num / (float) 1000;
            formatNum = String.format("%.2f", newNum) + "k";
        }
        return formatNum;
    }

    /**
     * 格式化数字，以w/k为单位
     *
     * @param num 待格式化数据
     * @return
     */
    @SuppressLint("DefaultLocale")
    public static String formatNum(long num) {
        String formatNum = num + "";
        if (num >= 10000) {
            float newNum = num / (float) 10000;
            formatNum = String.format("%.2f", newNum) + "w";
        } else if (num >= 1000) {
            float newNum = num / (float) 1000;
            formatNum = String.format("%.2f", newNum) + "k";
        }
        return formatNum;
    }

    /**
     * NumberFormat进行数据格式化转换[123,456,789.128]
     *
     * @param value        待转换数据
     * @param groupingUsed 是否以逗号隔开
     */
    public static String numberFormatConvertStr(double value, boolean groupingUsed) {
        NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(groupingUsed);
        // 结果未做任何处理
        return nf.format(value);
    }

    /**
     * NumberFormat进行数据格式化转换
     *
     * @param value        待转换数据
     * @param groupingUsed 是否以逗号隔开
     */
    public static String numberFormatConvertStr(float value, boolean groupingUsed) {
        NumberFormat nf = NumberFormat.getInstance();
        // 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
        nf.setGroupingUsed(groupingUsed);
        // 结果未做任何处理
        return nf.format(value);
    }

    /**
     * BigDecimal进行数据格式化转换
     *
     * @param value 待转换数据
     * @param scale 保留小数位
     */
    public static String bigDecimalConvertStr(double value, int scale) {
        BigDecimal d1 = new BigDecimal(Double.toString(value));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入
        return d1.divide(d2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * BigDecimal进行数据格式化转换
     *
     * @param value 待转换数据
     * @param scale 保留小数位
     */
    public static String bigDecimalConvertStr(float value, int scale) {
        BigDecimal d1 = new BigDecimal(Float.toString(value));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入
        return d1.divide(d2, scale, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 保留两位小数 1
     *
     * @param value 待转换数据
     */
    public static String bigDecimalConvertStr2(double value) {
        DecimalFormat format = new DecimalFormat("#,##0.00");
        return format.format(value);
    }

    /**
     * 保留三位小数 1
     *
     * @param value 待转换数据
     */
    public static String bigDecimalConvertStr3(int value) {
        DecimalFormat format = new DecimalFormat("#,##0.000");
        return format.format(value);
    }

    /**
     * 保留三位小数 2
     *
     * @param value 待转换数据
     */
    public static String bigDecimalConvertStr3(float value) {
        DecimalFormat format = new DecimalFormat("#,##0.000");
        return format.format(value);
    }

    /**
     * 保留三位小数 3
     *
     * @param value 待转换数据
     */
    public static String bigDecimalConvertStr3(double value) {
        DecimalFormat format = new DecimalFormat("#,##0.000");
        return format.format(value);
    }

    /**
     * 正则表达式去掉多余的.与0
     *
     * @param value 待处理数据
     */
    public static String removeNumber0(String value) {
        if (null != value && value.indexOf(".") > 0) {
            value = value.replaceAll("0+?$", "");// 去掉多余的0
            value = value.replaceAll("[.]$", "");// 如最后一位是.则去掉
        }
        return value;
    }

    /**
     * 正则表达式去掉多余的.与0
     *
     * @param value 待处理数据
     */
    public static String removeNumber0(double value) {
        return removeNumber0(value + "");
    }

    /**
     * 正则表达式去掉多余的.与0
     *
     * @param value 待处理数据
     */
    public static String removeNumber0(float value) {
        return removeNumber0(value + "");
    }

    /**
     * 将数字转换成百分比
     *
     * @param num 待转换数据
     */
    public static String numToPercentage(float num) {
        return removeNumber0(num * 100 + "") + "%";
    }

}
