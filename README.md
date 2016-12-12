# TinyLog
用于将数据写入到本地文件中(html格式)，支持tag标志，颜色，批量写入

# 初始化
```
   TinyLogConfig config = new TinyLogConfig(this);
   // 此句不写的话会有默认值
   config.buildFileCount(10).buildFileSize(100 * 1024).buildStorePath("");
   TinyLog.getInstance().init(config); 
```
# 单次写入
```
   TinyLog.getInstance().log("wodetianna");
   TinyLog.getInstance().logColor("MainActivity", "wodetianna", "#32acfb");
```

# 批量写入,开始设置的tag，颜色值有效，过程中只会写入内容
```
   TinyLog.getInstance().startWithSameTag("TAG", "#32fac5");
   TinyLog.getInstance().log("wodetianna");
   TinyLog.getInstance().logColor("MainActivity", "wodetianna", "#32acfb");
   TinyLog.getInstance().log("1111111111");
   TinyLog.getInstance().logColor("2222222222", "#1ab45c");
   TinyLog.getInstance().endWithSameTag();
```
# 引用方式
```
   compile 'com.tinylpc:tinyLog:1.0.0'
```
