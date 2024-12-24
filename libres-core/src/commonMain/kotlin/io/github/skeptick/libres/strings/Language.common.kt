package io.github.skeptick.libres.strings

import io.github.skeptick.libres.LibresSettings

public fun getCurrentLanguageCode(): String {
    return LibresSettings.languageCode ?: getPlatformLanguageCode()
}

public expect fun getPlatformLanguageCode(): String