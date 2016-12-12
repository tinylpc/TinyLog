package com.tiny.log;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by tiny on 16/12/12.
 */

public class TinyLogConfig {

    private Context context;
    /**
     * log保存路径
     */
    private String storePath;

    /**
     * 最大文件数量
     */
    private int fileCount = 10;

    /**
     * 最大文件大小
     */
    private long fileSize = 100 * 1024;


    public TinyLogConfig(Context context) {
        this.context = context;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            storePath = context.getExternalCacheDir().getPath() + File.separator + "tinyLog";
        } else {
            storePath = context.getCacheDir().getPath() + File.separator + "tinyLog";
        }

        File file = new File(storePath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 设置存储路径 
     *
     * @param storepath
     * @return
     */
    public TinyLogConfig buildStorePath(String storepath) {
        this.storePath = storepath;
        return this;
    }

    /**
     * 设置最大文件数量
     *
     * @param fileCount
     * @return
     */
    public TinyLogConfig buildFileCount(int fileCount) {
        this.fileCount = fileCount;
        return this;
    }

    /**
     * 设置单个文件最大存储大小 
     *
     * @param fileSize
     * @return
     */
    public TinyLogConfig buildFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public String getStorePath() {
        return storePath;
    }

    public int getFileCount() {
        return fileCount;
    }

    public long getFileSize() {
        return fileSize;
    }
}
