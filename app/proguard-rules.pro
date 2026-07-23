# ProGuard rules for Artier IDE Lite

# Keep Sora Editor classes
-keep class io.github.rosemoe.sora.** { *; }
-dontwarn io.github.rosemoe.sora.**

# Keep Termux classes
-keep class com.termux.** { *; }
-dontwarn com.termux.**

# Keep Room entities
-keep class com.artier.ide.lite.core.database.entity.** { *; }

# Keep data classes for serialization
-keep class com.artier.ide.lite.core.model.** { *; }
-keepclassmembers class com.artier.ide.lite.core.model.** {
    <fields>;
    <init>(...);
}

# Keep Hilt generated code
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# Keep enum entries
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Node.js daemon assets
-keep class com.artier.ide.lite.daemon.** { *; }
