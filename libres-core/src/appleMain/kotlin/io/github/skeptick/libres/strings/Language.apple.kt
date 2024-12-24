package io.github.skeptick.libres.strings

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

public actual fun getPlatformLanguageCode(): String {
    return NSLocale.currentLocale.languageCode
}