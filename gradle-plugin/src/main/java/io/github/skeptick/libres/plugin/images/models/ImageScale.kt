package io.github.skeptick.libres.plugin.images.models

import io.github.skeptick.libres.plugin.KotlinPlatform

@Suppress("EnumEntryName")
internal enum class ImageScale(vararg val supportedPlatforms: KotlinPlatform) {
    x1(KotlinPlatform.Android, KotlinPlatform.Apple, KotlinPlatform.Jvm, KotlinPlatform.Js),
    x1_5(KotlinPlatform.Android),
    x2(KotlinPlatform.Android, KotlinPlatform.Apple),
    x3(KotlinPlatform.Android, KotlinPlatform.Apple),
    x4(KotlinPlatform.Android);
}

internal val ImageScale.androidName: String
    get() = when (this) {
        ImageScale.x1 -> "mdpi"
        ImageScale.x1_5 -> "hdpi"
        ImageScale.x2 -> "xhdpi"
        ImageScale.x3 -> "xxhdpi"
        ImageScale.x4 -> "xxxhdpi"
    }

internal operator fun Int.times(scale: ImageScale): Int =
    when (scale) {
        ImageScale.x1 -> this
        ImageScale.x1_5 -> (this * 1.5).toInt()
        ImageScale.x2 -> this * 2
        ImageScale.x3 -> this * 3
        ImageScale.x4 -> this * 4
    }