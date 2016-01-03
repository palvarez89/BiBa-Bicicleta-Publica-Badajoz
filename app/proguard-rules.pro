# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/pedro/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# For RoboSpice
#Results classes that only extend a generic should be preserved as they will be pruned by Proguard
#as they are "empty", others are kept
#-keep class <your REST POJOs package>.**

#RoboSpice requests should be preserved in most cases
-keepclassmembers class biba.bicicleta.publica.badajoz.objects.Estacion** { *; }

#-keep class biba.bicicleta.publica.badajoz.** { *; }

#Warnings to be removed. Otherwise maven plugin stops, but not dangerous
-dontwarn android.support.**
-dontwarn com.sun.xml.internal.**
-dontwarn com.sun.istack.internal.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.springframework.**
-dontwarn java.awt.**
-dontwarn javax.security.**
-dontwarn java.beans.**
-dontwarn javax.xml.**
-dontwarn java.util.**
-dontwarn org.w3c.dom.**
-dontwarn com.google.common.**
-dontwarn com.octo.android.robospice.persistence.**

# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-support-v7-appcompat.pro

-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-support-design.pro
-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
-keep public class android.support.design.R$* { *; }

# https://github.com/spring-projects/spring-android/blob/master/test/spring-android-rest-template-test/proguard.cfg

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keepclassmembers public class org.springframework {
    public *;
}

# https://github.com/afollestad/drag-select-recyclerview/blob/master/sample/proguard-rules.pro

-keepattributes SourceFile,LineNumberTable
-keep class android.support.v7.internal.view.menu.** {*;}
-keep class android.support.v7.widget.SearchView {*;}
-keep class android.support.v7.widget.ActionMenuPresenter {*;}
-keep class android.support.v7.widget.ActionMenuView {*;}
-keep class android.support.v7.widget.Toolbar {*;}
#-dontwarn
#-ignorewarnings

# https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-support-v7-cardview.pro
# http://stackoverflow.com/questions/29679177/cardview-shadow-not-appearing-in-lollipop-after-obfuscate-with-proguard/29698051
-keep class android.support.v7.widget.RoundRectDrawable { *; }

# http://stackoverflow.com/questions/29715460/google-play-services-v23-proguard-configuration
# http://stackoverflow.com/questions/18646899/proguard-cant-find-referenced-class-com-google-android-gms-r

-keep public class com.google.android.gms.* { public *; }
-dontwarn com.google.android.gms.**

# From http://stackoverflow.com/questions/24057167/resttemplate-jackson-and-proguard
-keepattributes Signature

# Extra things to make it work.

-dontwarn com.octo.android.robospice.**
-keep class com.octo.android.robospice.** { *; }
-keep class org.codehaus.jackson.** { *; }
