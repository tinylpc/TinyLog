package com.tiny.log;

import android.text.TextUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tiny on 16/12/12.
 */
public class TinyLog {
    private static final String START_TAG = "----------%1$s";
    private static final String END_TAG = "----------end";
    private static final String START_FONT = "<font color=\"%1$s\">";
    private static final String END_FONT = "</font>";
    private static final String BLACK = "#000000";
    private static final String TAG = TinyLog.class.getSimpleName();
    private static TinyLog tinyLog;
    private TinyLogConfig tinyLogConfig;
    private String lastFileName;
    private boolean isStartWithSameTag = false;
    private String sameTag;
    private String sameColor;
    private StringBuilder sb;
    private String tempContent;

    public static TinyLog getInstance() {
        if (tinyLog == null) {
            synchronized (TinyLog.class) {
                if (tinyLog == null) {
                    tinyLog = new TinyLog();
                }
            }
        }
        return tinyLog;
    }

    private TinyLog() {

    }

    public void init(TinyLogConfig tinyLogConfig) {
        this.tinyLogConfig = tinyLogConfig;

        File file = new File(tinyLogConfig.getStorePath());
        long temp = 0;
        for (File f : file.listFiles()) {
            if (temp < f.lastModified()) {
                lastFileName = f.getName();
                temp = f.lastModified();
            }
        }
    }

    public void checkConfig() {
        if (tinyLogConfig == null) {
            throw new IllegalStateException("tinyLogConfig is not init");
        }
    }

    public void log(String content) {
        log(TAG, content);
    }

    public void log(String tag, String content) {
        logColor(tag, content, BLACK);
    }

    public void logColor(String content, String color) {
        logColor(TAG, content, color);
    }

    public void logColor(String tag, String content, String color) {

        checkConfig();

        if (isStartWithSameTag) {
            sb.append(content);
            sb.append("</br>");
        } else {
            File file;
            // 为空标示目录中还没有任何文件
            if (TextUtils.isEmpty(lastFileName)) {
                file = new File(tinyLogConfig.getStorePath(), String.valueOf(System.currentTimeMillis()) + ".html");
            } else {
                file = new File(tinyLogConfig.getStorePath(), lastFileName);
            }

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            lastFileName = file.getName();

            StringBuilder sb = new StringBuilder();
            sb.append(String.format(START_FONT, color));
            sb.append("</br>");
            sb.append(String.format(START_TAG, tag));
            sb.append("</br>");
            sb.append(formatTime(System.currentTimeMillis()));
            sb.append("</br>");
            sb.append(content);
            sb.append("</br>");
            sb.append(END_TAG);
            sb.append("</br>");
            sb.append(END_FONT);
            sb.append("</br>");

            tempContent = sb.toString();
            File tempFile = new File(tinyLogConfig.getStorePath(), "temp.html");
            write(tempFile, tempContent, false);

            //超过单个文件的最大限制
            if (file.length() + tempFile.length() > tinyLogConfig.getFileSize()) {
                // 存在一个临时文件，需要减去
                if (getFileCount(new File(tinyLogConfig.getStorePath())) - 1 >= tinyLogConfig.getFileCount()) {
                    getOldestFile(new File(tinyLogConfig.getStorePath())).delete();
                }
                lastFileName = "";
                logColor(tag, content, color);
            } else {
                tempFile.delete();
                write(file, tempContent, true);
            }
        }
    }

    public void startWithSameTag() {
        startWithSameTag(TAG);
    }

    public void startWithSameTag(String tag) {
        startWithSameTag(tag, BLACK);
    }

    public void startWithSameTag(String tag, String color) {

        checkConfig();
        sb = new StringBuilder();
        isStartWithSameTag = true;
        this.sameTag = tag;
        this.sameColor = color;
    }

    public void endWithSameTag() {
        isStartWithSameTag = false;
        logColor(sameTag, sb.toString(), sameColor);
    }

    /**
     * 追加模式写入文件
     *
     * @param file
     * @param conent
     */
    private void write(File file, String conent, boolean append) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, append)));
            out.write(conent);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String formatTime(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        java.util.Date dt = new Date(time);
        return sdf.format(dt);
    }

    /**
     * 检查文件数量是否还在限制范围内
     *
     * @param folder
     * @return
     */
    private int getFileCount(File folder) {
        return folder.listFiles().length;
    }

    /**
     * 获取时间最早的文件
     *
     * @param folder
     * @return
     */
    private File getOldestFile(File folder) {
        File[] files = folder.listFiles();
        File file = files[0];
        long oldest = files[0].lastModified();
        for (File f : folder.listFiles()) {
            if (oldest > f.lastModified()) {
                file = f;
                oldest = f.lastModified();
            }
        }

        return file;
    }
}
