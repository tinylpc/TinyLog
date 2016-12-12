package com.tiny.tinylog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.tiny.log.TinyLog;
import com.tiny.log.TinyLogConfig;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TinyLogConfig config = new TinyLogConfig(this);
        config.buildFileCount(10).buildFileSize(100 * 1024).buildStorePath("");
        TinyLog.getInstance().init(config);

        TinyLog.getInstance().startWithSameTag("TAG", "#32fac5");
        TinyLog.getInstance().log("wodetianna");
        TinyLog.getInstance().logColor("MainActivity", "wodetianna", "#32acfb");
        TinyLog.getInstance().log("1111111111");
        TinyLog.getInstance().logColor("2222222222", "#1ab45c");
        TinyLog.getInstance().endWithSameTag();
    }
}
