package io.github.skeptick.libres.strings

import java.util.Locale

actual fun getPlatformLanguageCode(): String {
    return Locale.getDefault().language
}