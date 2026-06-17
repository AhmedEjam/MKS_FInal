# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# --- General Attributes ---
# Retain metadata for reflection and generic type resolution
-keepattributes RuntimeVisibleAnnotations,RuntimeInvisibleAnnotations,Signature,InnerClasses,EnclosingMethod

# --- AndroidX / Safety ---
# Keep classes and members annotated with @Keep
-keep @androidx.annotation.Keep class * {*;}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keepclasseswithmembers class * {
    @androidx.annotation.Keep <fields>;
}

# --- Data Layer (MKS Specific) ---
# Protect the entire data layer as requested (Broad rule for safety)
# This prevents obfuscation issues with Room entities, DTOs, and repositories.
-keep class com.ahmedyejam.mks.data.** { *; }

# --- Kotlin Serialization ---
# Keep serializable classes and their companion objects to ensure stable JSON parsing
-keep @kotlinx.serialization.Serializable class * { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}

# --- Room & Moshi ---
# While libraries publish consumer rules, these ensure stability in complex R8 configurations
-keep class **JsonAdapter { *; }
-keep class **JsonAdapterKt { *; }
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * implements androidx.room.Dao

# --- Third Party Fixes / Dontwarns ---
# Apache POI may reference optional XML/security backends depending on workbook contents.
-dontwarn org.apache.xmlbeans.**
-dontwarn org.bouncycastle.**
-dontwarn org.apache.commons.compress.**

# Fix for missing classes in log4j, findbugs, and poi
-dontwarn aQute.bnd.annotation.**
-dontwarn edu.umd.cs.findbugs.annotations.**
-dontwarn java.awt.**
-dontwarn javax.xml.stream.**
-dontwarn org.osgi.framework.**

# Apache POI & Aalto XML
-keep class com.fasterxml.aalto.** { *; }
-keep class javax.xml.stream.** { *; }
-keep class org.apache.poi.** { *; }
-keep interface org.apache.poi.** { *; }
-keep class org.apache.xmlbeans.** { *; }
-keep interface org.apache.xmlbeans.** { *; }
-keep class schemaorg_apache_xmlbeans.** { *; }

# Preserve ServiceLoader files for POI providers
-keepclassmembers class * {
    *** *Service(...);
}
