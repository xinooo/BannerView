# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#-----------------混淆配置设定------------------------------------------------------------------------
-optimizationpasses 5                                                       #指定代碼壓縮級別
-dontusemixedcaseclassnames                                                 #混淆時不會產生形形色色的類別名
-dontskipnonpubliclibraryclasses                                            #指定不忽略非公共類別庫
-dontpreverify                                                              #不預校驗，如果需要預校驗，是-dontoptimize
-ignorewarnings                                                             #屏蔽警告
-verbose                                                                    #混淆時記錄日誌
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*    #最佳化

#忽略警告
-ignorewarnings
#記錄產生的日誌資料,gradle build時在本項目根目錄輸出
#apk 套件內所有 class 的內部結構
-dump class_files.txt
#未混淆的類別和成員
-printseeds seeds.txt
#列出從 apk 中刪除的程式碼
-printusage unused.txt
#混淆前後的映射
-printmapping mapping.txt