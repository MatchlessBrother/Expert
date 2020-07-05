    
# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
# For more details, see
# http://developer.android.com/guide/developing/tools/proguard.html
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


####################################################################################################
##########################################基本属性配置###############################################
-verbose
-dontoptimize
-dontpreverify
-ignorewarnings
-optimizationpasses 5
-allowaccessmodification
-useuniqueclassmembernames
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-renamesourcefileattribute SourceFile
-dontskipnonpubliclibraryclassmembers
-keepattributes SourceFile,LineNumberTable
-keepattributes *Annotation*,InnerClasses,Signature,EnclosingMethod
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*



####################################################################################################
##########################################本地混淆配置###############################################
#----------------------------------android.support包中的所有类和接口---------------------------------
-dontwarn android.support.**
-keep class android.support.**{*;}
-keep interface android.support.**{*;}
-keep class * extends android.support.**{*;}
-keep class * implements android.support.**{*;}
#----------------------------------------Application与四大组件---------------------------------------
-keep class * extends android.app.Fragment{*;}
-keepnames class * extends android.app.Application{*;}
-keepclassmembernames class * extends android.app.Service{*;}
-keepclassmembernames class * extends android.content.ContentProvider{*;}
-keepclassmembernames class * extends android.content.BroadcastReceiver{*;}
-keepclassmembernames class * extends android.app.Activity{public void *(android.view.View);}
#-----------------------View控件(系统View与自定义View等等)以及上面它的OnClick函数---------------------
-keep class android.view.View{*;}
-keep class * extends android.view.View{
    *** is*();
    *** get*();
    void set*(...);
    public <init>(android.content.Context);
    public <init>(android.content.Context,android.util.AttributeSet);
    public <init>(android.content.Context,android.util.AttributeSet,int);
    public <init>(android.content.Context,android.util.AttributeSet,int,int);
}
#-------------------------------Webview和WebviewClient以及Js的交互-----------------------------------
-keep class android.webkit.WebView{*;}
-keep class * extends android.webkit.WebView{*;}
-keepclassmembers class * extends android.webkit.webViewClient{
    public void *(android.webkit.webView,java.lang.String);
}
-keepclassmembers class * extends android.webkit.webViewClient{
    public boolean *(android.webkit.WebView,java.lang.String);
    public void *(android.webkit.WebView,java.lang.String,android.graphics.Bitmap);
}
-keepclassmembers class fqcn.of.javascript.interface.for.webview{
    public *;
}
#-----------------------------------------------R资源类---------------------------------------------
-keepclassmembers class **.R$*{
    public static <fields>;
}
#----------------------------------------JNI保留本地所有native方法-----------------------------------
-keepclasseswithmembernames class *{
    native <methods>;
}
#------------------------------------------------枚举类---------------------------------------------
-keepclassmembers enum *{
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#------------------------------------------禁止Log打印任何数据---------------------------------------
-assumenosideeffects class android.util.Log{
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }
#--------------------------------------------Serializable-------------------------------------------
-keepclassmembers class * implements java.io.Serializable{
    java.lang.Object readResolve();
    java.lang.Object writeReplace();
    static final long serialVersionUID;
    private void readObject(java.io.ObjectInputStream);
    private void writeObject(java.io.ObjectOutputStream);
    private static final java.io.ObjectStreamField[] serialPersistentFields;
}
#----------------------------------------------Parcelable-------------------------------------------
-keepclassmembers class * implements android.os.Parcelable{
    static ** CREATOR;
}
#-------------------------------------------MVP模式禁止混淆-----------------------------------------


#---------------------------------------------Model数据类-------------------------------------------


#----------------------------------------------反射对象类-------------------------------------------


#----------------------------------------------回调函数类-------------------------------------------


#-------------------------------------------------其他----------------------------------------------
-keepnames class com.expert.cleanup.**{*;}
-keepnames interface com.expert.cleanup.**{*;}

####################################################################################################
##########################################远端混淆配置###############################################
#-------------------------------------------第三方库-------------------------------------------------
#-----------Pinyin4j--------#
-dontwarn com.hp.hpl.sparta.**
-keepnames class com.hp.hpl.sparta.**{*;}
#--------FlycoDialog--------#
-dontwarn com.flyco.**
-keepnames class com.flyco.**{*;}
-keepnames interface com.flyco.**{*;}
-keepnames class * extends com.flyco.**{*;}
-keepnames class * implements com.flyco.**{*;}
-keepnames interface * extends com.flyco.**{*;}
#---------Bga-Banner--------#
-dontwarn cn.bingoogolapple.bgabanner.**
-keepnames class cn.bingoogolapple.bgabanner.**{*;}
-keepnames interface cn.bingoogolapple.bgabanner.**{*;}
#--------Immersionbar-------#
-dontwarn com.gyf.barlibrary.**
-keepnames class com.gyf.barlibrary.**{*;}
-keepnames interface com.gyf.barlibrary.**{*;}
-keepnames class * extends com.gyf.barlibrary.**{*;}
-keepnames class * implements com.gyf.barlibrary.**{*;}
-keepnames interface * extends com.gyf.barlibrary.**{*;}
#------AndroidAutoSize------#
-dontwarn me.jessyan.autosize.**
-keep class me.jessyan.autosize.**{*;}
-keep interface me.jessyan.autosize.**{*;}
-keepnames class * extends me.jessyan.autosize.**{*;}
-keepnames interface * extends me.jessyan.autosize.**{*;}
-keepnames interface * implements me.jessyan.autosize.**{*;}
#---------PhotoView---------#
-dontwarn com.github.chrisbanes.photoview.**
-keepnames class com.github.chrisbanes.photoview.**{*;}
-keepnames interface com.github.chrisbanes.photoview.**{*;}
-keepnames class * extends com.github.chrisbanes.photoview.**{*;}
-keepnames class * implements com.github.chrisbanes.photoview.**{*;}
-keepnames interface * extends com.github.chrisbanes.photoview.**{*;}
#--BaseRecyclerViewAdapterHelper--#
-dontwarn com.chad.library.adapter.**
-keep class com.chad.library.adapter.**{*;}
-keep public class * extends com.chad.library.adapter.base.BaseViewHolder
-keep public class * extends com.chad.library.adapter.base.BaseQuickAdapter
-keepclassmembers class **$** extends com.chad.library.adapter.base.BaseViewHolder{<init>(...);}

-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keep public class com.expert.cleanup.R$*{
    public static final int *;
}