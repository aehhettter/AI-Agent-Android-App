# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# signingConfigs blocks in the android section of this build.gradle.

-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class com.example.aiagent.** { *; }
-keep interface * { *; }
