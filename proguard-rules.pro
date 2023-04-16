#扁平包结构
-flattenpackagehierarchy

#指定压缩级别
-optimizationpasses 5


#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#指定不去忽略非公共的库的类
-dontskipnonpubliclibraryclasses

#混淆时不使用大小写混合，混淆后的类名为小写(大小写混淆容易导致class文件相互覆盖）
-dontusemixedcaseclassnames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#保持行号和源码
-keepattributes SourceFile, LineNumberTable
#保持泛型
-keepattributes Signature
#保持注解
-keepattributes *Annotation*,InnerClasses

#保留Jetbrains SDK
-keep class com.intellij.** { *; }
-dontwarn com.intellij.**

-keep class org.jetbrains.annotations.** { *; }
-dontwarn org.jetbrains.annotations.**

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

#保留类的protected属性和方法
-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

#以下Class不进行混淆
-keep class io.reactivex.** { *; }
-dontwarn io.reactivex.**

-keep class com.alibaba.** { *; }
-dontwarn com.alibaba.**

-keep class  okhttp3.** { *; }
-dontwarn  okhttp3.**

-keep class  retrofit2.** { *; }
-dontwarn  retrofit2.**

-keep class  com.guohanlin.ui.** { *; }
-dontwarn  com.guohanlin.ui.**

-keep class  com.guohanlin.utils.** { *; }
-dontwarn  com.guohanlin.utils.**

-keep class  com.guohanlin.model.** { *; }
-dontwarn  com.guohanlin.model.**

# Kotlin相关
-dontwarn kotlin.**
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

#保持所有实现Serializable接口的类成员
-keepclassmembers class * implements java.io.Serializable {
   static final long serialVersionUID;
   private static final java.io.ObjectStreamField[] serialPersistentFields;
   private void writeObject(java.io.ObjectOutputStream);
   private void readObject(java.io.ObjectInputStream);
   java.lang.Object writeReplace();
   java.lang.Object readResolve();
}

