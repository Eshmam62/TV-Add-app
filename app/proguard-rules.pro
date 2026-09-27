# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in the SDK tools proguard-defaults.txt file.

# Keep ExoPlayer classes
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# Keep Gson serialization
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.example.smarttvadplayer.domain.model.** { *; }
