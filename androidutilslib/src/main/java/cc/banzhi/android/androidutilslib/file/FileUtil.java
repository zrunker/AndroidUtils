package cc.banzhi.android.androidutilslib.file;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * 文件工具类-增删改查，需要添加三个权限 WRITE_EXTERNAL_STORAGE、READ_EXTERNAL_STORAGE、MOUNT_UNMOUNT_FILESYSTEMS
 * <p>
 * Created by 邹峰立 on 2017/7/11.
 */
public class FileUtil {
    // 内存卡路径
    public static String SDPATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    // 工程文件路径
    public static String ZFILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "ZFile" + File.separator;
    // 文件大小单位
    public static final int SIZETYPE_B = 1;// 获取文件大小单位为B
    public static final int SIZETYPE_KB = 2;// 获取文件大小单位为KB
    public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB
    public static final int SIZETYPE_GB = 4;// 获取文件大小单位为GB

    public static String getSdPath(Context context) {
        String sdPath = SDPATH;
        File file = context.getApplicationContext().getExternalFilesDir(null);
        if (file != null) {
            boolean bool = file.exists();
            if (!bool)
                bool = file.mkdirs();
            if (bool)
                sdPath = file.getAbsolutePath();
        }
        return sdPath;
    }

    /**
     * 创建多层目录
     *
     * @param path 完整文件路径
     */
    public static File createSDDirs(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 判断SD卡是否存在
            File dir = new File(path);
            boolean bool = true;
            if (!dir.exists()) bool = dir.mkdirs();
            if (!bool)
                return null;
            else
                return dir;
        }
        return null;
    }

    /**
     * 创建单层目录
     *
     * @param path 完整文件路径
     */
    public static File createSDDir(String path) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // 判断SD卡是否存在
            File dir = new File(path);
            boolean bool = true;
            if (!dir.exists()) bool = dir.mkdir();
            if (!bool)
                return null;
            else
                return dir;
        }
        return null;
    }

    /**
     * 创建文件-根据当前年月日时分秒（时间戳）生成
     */
    public static File createTimeMillisFile() {
        try {
            long timeMillis = System.currentTimeMillis();
            String filePath = SDPATH + File.separator + timeMillis;
            File file = new File(filePath);
            boolean bool = file.createNewFile();
            if (!bool)
                return null;
            else
                return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建文件-根据文件名生成
     *
     * @param fileName 文件名（带后缀）
     */
    public static File createNameFile(String fileName) {
        try {
            String filePath = SDPATH + File.separator + fileName;
            File file = new File(filePath);
            boolean bool = file.createNewFile();
            if (!bool)
                return null;
            else
                return file;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除文件
     *
     * @param path 完整文件路径
     */
    public static boolean delFile(String path) {
        File file = new File(path);
        return file.isFile() && file.exists() && file.delete();
    }

    /**
     * 删除文件夹（目录），以及内部所有文件
     *
     * @param path 完整文件路径
     */
    public static boolean deleteDir(String path) {
        try {
            File dir = new File(path);
            if (!dir.exists() || !dir.isDirectory())
                return false;
            for (File file : dir.listFiles()) {
                if (file != null) {
                    if (file.isFile()) {// 删除所有文件
                        if (!file.delete()) return false;
                    } else if (file.isDirectory()) {// 递规的方式删除文件夹
                        deleteDir(file.getAbsolutePath());
                    }
                }
            }
            return dir.delete();// 删除目录本身
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 读取文件-将应用内文件读到内存-输入流（流向内存）-子线程
     * 文件路径如：/data/data/cc.ibooker.zfile/files/test.txt
     *
     * @param context  上下文对象
     * @param filename 应用内文件名
     */
    public static String readFile(Context context, String filename) {
        FileInputStream fis = null;
        byte[] buffer = null;
        try {
            fis = context.openFileInput(filename);
            buffer = new byte[fis.available()];
            fis.read(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new String(buffer != null ? buffer : new byte[0]);
    }

    /**
     * 写入文件-将内存写入应用内文件-输出流（流出内存）-子线程
     *
     * @param context  上下文对象
     * @param content  要写入的字符串
     * @param filename 应用内文件名
     * @param mode     写入模式，MODE_PRIVATE、MODE_APPEND
     */
    public static void writeFile(Context context, String content, String filename, int mode) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(filename, mode);
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 复制单个文件（复制文件内容）-子线程
     *
     * @param oldPath String 原文件路径 如：c:/fqf.txt
     * @param newPath String 复制后路径 如：f:/fqf.txt
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int byteRead;
            File oldFile = new File(oldPath);
            if (oldFile.exists()) {// 文件存在时
                InputStream inStream = new FileInputStream(oldPath);// 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1024 * 5];
                while ((byteRead = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteRead);
                }
                inStream.close();
                fs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容-子线程
     *
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     */
    public static void copyFolder(String oldPath, String newPath) {
        try {
            File newFile = new File(newPath);
            boolean bool = newFile.exists();
            if (!bool) {// 如果文件夹不存在 则建立新文件夹
                bool = newFile.mkdirs();
            }
            if (bool) {
                File oldFile = new File(oldPath);
                String[] files = oldFile.list();
                File temp;
                for (String file : files) {
                    if (oldPath.endsWith(File.separator)) {
                        temp = new File(oldPath + file);
                    } else {
                        temp = new File(oldPath + File.separator + file);
                    }
                    if (temp.isFile()) {
                        FileInputStream input = new FileInputStream(temp);
                        FileOutputStream output = new FileOutputStream(newPath + File.separator + temp.getName());
                        byte[] b = new byte[1024 * 5];
                        int len;
                        while ((len = input.read(b)) != -1) {
                            output.write(b, 0, len);
                        }
                        output.flush();
                        output.close();
                        input.close();
                    }
                    if (temp.isDirectory()) {// 如果是子文件夹
                        copyFolder(oldPath + File.separator + file, newPath + File.separator + file);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 判断文件/目录是否存在
     *
     * @param path 完整文件路径
     */
    public static boolean isFileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 获取文件/文件夹的指定单位的大小
     *
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        long blockSize = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    blockSize = getFileSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize, sizeType);
    }

    /**
     * 调用此方法自动计算文件/文件夹的大小
     *
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath) {
        long blockSize = 0;
        try {
            File file = new File(filePath);
            if (file.exists()) {
                if (file.isDirectory()) {
                    blockSize = getFileSizes(file);
                } else {
                    blockSize = getFileSize(file);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formatFileSize(blockSize);
    }

    /**
     * 获取指定文件大小（B）
     *
     * @param file 指定文件
     */
    public static long getFileSize(File file) {
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file.exists() && file.isFile()) {
                fis = new FileInputStream(file);
                size = fis.available();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取指定文件夹大小（B）
     *
     * @param files 指定文件夹
     */
    public static long getFileSizes(File files) {
        long size = 0;
        try {
            if (files.exists()) {
                File[] fList = files.listFiles();
                if (fList != null)
                    for (File file : fList) {
                        if (file.isDirectory()) {
                            size = size + getFileSizes(file);
                        } else {
                            size = size + getFileSize(file);
                        }
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 转换文件大小（取最大单位）（保留两位小数）
     *
     * @param fileSize 文件大小
     */
    public static String formatFileSize(long fileSize) {
        String fileSizeStr = null;
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            if (fileSize <= 0) {
                fileSizeStr = "0B";
            } else if (fileSize < 1024) {
                fileSizeStr = df.format((double) fileSize) + "B";
            } else if (fileSize < 1048576) {
                fileSizeStr = df.format((double) fileSize / 1024) + "KB";
            } else if (fileSize < 1073741824) {
                fileSizeStr = df.format((double) fileSize / 1048576) + "MB";
            } else {
                fileSizeStr = df.format((double) fileSize / 1073741824) + "GB";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileSizeStr;
    }

    /**
     * 转换文件大小，指定转换的单位（保留两位小数）
     *
     * @param fileSize 文件大小
     * @param sizeType 文件大小单位
     */
    public static double formatFileSize(long fileSize, int sizeType) {
        try {
            DecimalFormat df = new DecimalFormat("#.00");
            double fileSizeLong = 0;
            switch (sizeType) {
                case SIZETYPE_B:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize));
                    break;
                case SIZETYPE_KB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1024));
                    break;
                case SIZETYPE_MB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1048576));
                    break;
                case SIZETYPE_GB:
                    fileSizeLong = Double.valueOf(df.format((double) fileSize / 1073741824));
                    break;
                default:
                    break;
            }
            return fileSizeLong;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取本应用内部缓存大小（B）
     *
     * @param context 上下文对象
     */
    public static long getTotalCacheSize(Context context) {
        long cacheSize = 0;
        try {
            cacheSize = getFileSizes(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFileSizes(context.getExternalCacheDir());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cacheSize;
    }

    /**
     * 获取本应用内部缓存大小（格式化）
     *
     * @param context 上下文对象
     */
    public static String getFormatTotalCacheSize(Context context) {
        try {
            long cacheSize = getFileSizes(context.getCacheDir());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                cacheSize += getFileSizes(context.getExternalCacheDir());
            }
            return formatFileSize(cacheSize);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 清除本应用内部缓存
     *
     * @param context 上下文对象
     */
    public static void clearAllCache(Context context) {
        try {
            deleteDir(context.getCacheDir().getAbsolutePath());
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                if (context.getExternalCacheDir() != null)
                    deleteDir(context.getExternalCacheDir().getAbsolutePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除本应用SharedPreference(/data/data/com.xxx.xxx/sharedprefs)
     *
     * @param context 上下文对象
     */
    public static Boolean cleanSharedPreference(Context context) {
        return deleteDir(new File(File.separator + "data" + File.separator + "data" + File.separator + context.getPackageName() + File.separator + "sharedprefs").getAbsolutePath());
    }

    /**
     * 按名字清除本应用数据库
     *
     * @param context 上下文对象
     * @param dbName  数据库名
     */
    public static Boolean delDatabaseByName(Context context, String dbName) {
        return context.deleteDatabase(dbName);
    }

    /**
     * 打开指定文件
     *
     * @param file 指定文件
     */
    public static void openFile(Context context, File file) {
        if (file.exists()) {
            // 获取文件路径
            String filePath = file.getAbsolutePath();
            // 获取文件后缀
            String suffix = filePath.substring(filePath.lastIndexOf('.')).toLowerCase(Locale.US);
            try {
                // 获取MIME映射信息
                MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
                String temp = suffix.substring(1);
                String mime = mimeTypeMap.getMimeTypeFromExtension(temp);

                // 打开文件
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), mime);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(context, "无法打开后缀名为." + suffix + "的文件！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 获取目录下所有文件
     *
     * @param path 路径
     */
    public static File[] getFiles(String path) {
        File file = new File(path);
        return file.listFiles();
    }

    /**
     * 根据文件路径创建文件 - 前提条件是目录存在
     *
     * @param filePath 文件路径
     */
    public static File createFile(String filePath) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            try {
                File file = new File(filePath);
                boolean bool = file.createNewFile();
                if (bool) return file;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 删除文件夹（目录）/文件，以及内部所有文件
     *
     * @param path 完整文件路径
     */
    public static boolean deleteDirs(String path) {
        try {
            File dirs = new File(path);
            if (!dirs.exists())
                return false;
            if (dirs.isDirectory()) {
                for (File file : dirs.listFiles()) {
                    if (file != null) {
                        if (file.isFile()) {// 删除所有文件
                            if (!file.delete()) return false;
                        } else if (file.isDirectory()) {// 递规的方式删除文件夹
                            deleteDir(file.getAbsolutePath());
                        }
                    }
                }
            }
            return dirs.delete();// 删除目录本身
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取指定文件夹大小（B）
     *
     * @param files 指定文件夹
     */
    public static long getDirSize(File files) {
        long size = 0;
        try {
            if (files != null && files.exists()) {
                File[] fList = files.listFiles();
                if (fList != null) {
                    for (File file : fList) {
                        if (file.isDirectory()) {
                            size = size + getDirSize(file);
                        } else {
                            size = size + getFileSize(file);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    // 获取文件或文件夹大小
    public static long getFileSize2(File file) {
        long size = 0;
        if (file != null && file.exists()) {
            if (file.isFile())
                size = getFileSize(file);
            else
                size = getDirSize(file);
        }
        return size;
    }

    /**
     * 获取文件目录下文件信息
     *
     * @param path 路径
     */
    public static ArrayList<FileInfoBean> getFileInfos(String path) {
        ArrayList<FileInfoBean> fileInfoBeans = new ArrayList<>();
        // 获取文件
        File files = new File(path);
        if (files.exists()) {
            // 获取文件目录
            File[] fileList = files.listFiles();
            // 判断文件是否为目录文件
            if (files.isDirectory() && fileList != null) {
                for (File file1 : fileList) {
                    String fileName = file1.getName();
                    String filePath = file1.getAbsolutePath();
                    long fileSize = getFileSize2(file1);
                    long createTime = file1.lastModified();
                    fileInfoBeans.add(new FileInfoBean(fileName, filePath, fileSize, createTime));
                }
            } else {// 非目录或者空目录
                String fileName = files.getName();
                String filePath = files.getAbsolutePath();
                long fileSize = getDirSize(files);
                long createTime = files.lastModified();
                fileInfoBeans.add(new FileInfoBean(fileName, filePath, fileSize, createTime));
            }
        }
        return fileInfoBeans;
    }

    /**
     * 根据文件地址获取改文件夹下所以一级目录信息
     *
     * @param fordsPath 文件夹地址
     */
    public static ArrayList<FoldersInfoBean> getFoldersInfoList(@NonNull String fordsPath) {
        ArrayList<FoldersInfoBean> list = new ArrayList<>();
        File file = new File(fordsPath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File file1 : files) {
                    if (file1.exists() && file1.isDirectory()) {
                        FoldersInfoBean foldersInfoBean = new FoldersInfoBean();
                        foldersInfoBean.setFoldersPath(file1.getAbsolutePath());
                        foldersInfoBean.setFoldersCreateTime(file1.lastModified());
                        foldersInfoBean.setFolderName(file1.getName());
                        list.add(foldersInfoBean);
                    }
                }
        }
        return list;
    }

    /**
     * 获取单个文件的MD5值
     *
     * @param file 待获取文件
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return bytesToHexString(digest.digest());
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * 写入SD卡文件 - 子线程
     *
     * @param content 写入文件内容
     * @param file    待写入文件
     * @return 是否写入成功
     */
    public static boolean writeSdData(String content, File file) {
        boolean bool = false;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(file);
            osw = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
            osw.write(content);
            osw.flush();
            bool = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (osw != null)
                    osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bool;
    }
}