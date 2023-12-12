package io.github.skeptick.libres.strings

import io.github.skeptick.libres.LibresSettings

fun getCurrentLanguageCode(): String {
    return LibresSettings.languageCode ?: getPlatformLanguageCode()
}

expect fun getPlatformLanguageCode(): String