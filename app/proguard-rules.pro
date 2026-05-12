# ─── Media3 / Transformer ────────────────────────────────────────────────────
-keep class androidx.media3.** { *; }
-dontwarn androidx.media3.**

# ─── Hilt ────────────────────────────────────────────────────────────────────
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-dontwarn dagger.hilt.**

# ─── Gson ────────────────────────────────────────────────────────────────────
-keep class com.google.gson.** { *; }
-keep class com.clickpost.app.data.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# ─── Coil ────────────────────────────────────────────────────────────────────
-keep class coil.** { *; }
-dontwarn coil.**

# ─── Kotlin coroutines ───────────────────────────────────────────────────────
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**

# ─── General Android ─────────────────────────────────────────────────────────
-keepattributes EnclosingMethod
-keepattributes InnerClasses
