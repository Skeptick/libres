package io.github.skeptick.libres.strings

import androidx.core.os.LocaleListCompat

actual fun getPlatformLanguageCode(): String {
    return LocaleListCompat.getDefault()[0]?.language ?: "undefined"
}