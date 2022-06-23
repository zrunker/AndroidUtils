package cc.banzhi.android.androidutilslib.file;

import androidx.annotation.NonNull;

/**
 * 本地文件夹相关信息
 *
 * @author 邹峰立
 */
public class FoldersInfoBean {
    private String foldersPath;// 文件夹路径
    private long foldersCreateTime;// 文件夹创建时间
    private String folderName;// 文件名
    // 格式化本地数据
    private String formatFoldersCreateTime;// 格式化文件夹创建时间

    public String getFoldersPath() {
        return foldersPath;
    }

    public void setFoldersPath(String foldersPath) {
        this.foldersPath = foldersPath;
    }

    public long getFoldersCreateTime() {
        return foldersCreateTime;
    }

    public void setFoldersCreateTime(long foldersCreateTime) {
        this.foldersCreateTime = foldersCreateTime;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getFormatFoldersCreateTime() {
        return formatFoldersCreateTime;
    }

    public void setFormatFoldersCreateTime(String formatFoldersCreateTime) {
        this.formatFoldersCreateTime = formatFoldersCreateTime;
    }

    @NonNull
    @Override
    public String toString() {
        return "FoldersInfoBean{" +
                "foldersPath='" + foldersPath + '\'' +
                ", foldersCreateTime=" + foldersCreateTime +
                ", folderName='" + folderName + '\'' +
                ", formatFoldersCreateTime='" + formatFoldersCreateTime + '\'' +
                '}';
    }
}
